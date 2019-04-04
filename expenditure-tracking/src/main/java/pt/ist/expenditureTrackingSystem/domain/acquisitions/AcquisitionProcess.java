/*
 * @(#)AcquisitionProcess.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.User;

import module.finance.util.Money;
import module.workflow.domain.ProcessFile;
import module.workflow.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

@ClassNameBundle(bundle = "ExpenditureResources")
/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * @author João Alfaiate
 * 
 */
public abstract class AcquisitionProcess extends AcquisitionProcess_Base {

    public final static String SUPPLIER_METADATA_KEY = "Fornecedor";

    public AcquisitionProcess() {
        super();
        super.setSkipSupplierFundAllocation(Boolean.FALSE);
        setProcessNumber(constructProcessNumber());
    }

    protected String constructProcessNumber() {
        final ExpenditureTrackingSystem instance = getExpenditureTrackingSystem();
        if (instance.hasProcessPrefix()) {
            return instance.getInstitutionalProcessNumberPrefix() + "/" + getYear() + "/" + getAcquisitionProcessNumber();
        }
        return getYear() + "/" + getAcquisitionProcessNumber();
    }

    public boolean isAvailableForCurrentUser() {
        final Person loggedPerson = getLoggedPerson();
        return loggedPerson != null && isAvailableForPerson(loggedPerson);
    }

    public boolean isAvailableForPerson(final Person person) {
        final User user = person.getUser();
        return ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user)
                || ExpenditureTrackingSystem.isAcquisitionCentralManagerGroupMember(user)
                || ExpenditureTrackingSystem.isAccountingManagerGroupMember(user)
                || ExpenditureTrackingSystem.isProjectAccountingManagerGroupMember(user)
                || ExpenditureTrackingSystem.isTreasuryMemberGroupMember(user)
                || ExpenditureTrackingSystem.isAcquisitionsProcessAuditorGroupMember(user)
                || ExpenditureTrackingSystem.isFundCommitmentManagerGroupMember(user) || getRequestor() == person
                || isTakenByPerson(person.getUser()) || getRequestingUnit().isResponsible(person)
                || isResponsibleForAtLeastOnePayingUnit(person) || isAccountingEmployee(person)
                || isProjectAccountingEmployee(person) || isTreasuryMember(person) || isObserver(person);
    }

    @Override
    public boolean isAccessible(User user) {
        return user != null && isAvailableForPerson(user.getExpenditurePerson());
    }

    @Override
    public boolean isActive() {
        return getLastAcquisitionProcessState().isActive();
    }

    public AcquisitionProcessState getAcquisitionProcessState() {
        return getLastAcquisitionProcessState();
    }

    protected AcquisitionProcessState getLastAcquisitionProcessState() {
        final ProcessState state = getCurrentProcessState();
        return (AcquisitionProcessState) (state == null ? getProcessStatesSet().stream().max(ProcessState.COMPARATOR_BY_WHEN).orElse(null) : state);
    }

    public AcquisitionProcessStateType getAcquisitionProcessStateType() {
        return getLastAcquisitionProcessState().getAcquisitionProcessStateType();
    }

    @Override
    public boolean isPendingApproval() {
        return getLastAcquisitionProcessState().isPendingApproval();
    }

    public boolean isApproved() {
        final AcquisitionProcessStateType acquisitionProcessStateType = getAcquisitionProcessStateType();
        return acquisitionProcessStateType.compareTo(AcquisitionProcessStateType.SUBMITTED_FOR_FUNDS_ALLOCATION) <= 0
                && isActive();
    }

    public boolean isAllocatedToSupplier() {
        return getLastAcquisitionProcessState().isAllocatedToSupplier();
    }

    public boolean isAllocatedToUnit() {
        return getLastAcquisitionProcessState().isAllocatedToUnit();
    }

    @Override
    public boolean isPayed() {
        return getLastAcquisitionProcessState().isPayed();
    }

    public boolean isAllocatedToUnit(Unit unit) {
        return isAllocatedToUnit() && getPayingUnitStream().anyMatch(u -> u == unit);
    }

    public boolean isAcquisitionProcessed() {
        return getLastAcquisitionProcessState().isAcquisitionProcessed();
    }

    public boolean isInvoiceReceived() {
        return getLastAcquisitionProcessState().isInvoiceReceived();
    }

    public boolean hasReceivedInvoices() {
        return getFileStream(AcquisitionInvoice.class)
            .map(f -> (AcquisitionInvoice) f)
            .anyMatch(i -> i.getState() == AcquisitionInvoiceState.RECEIVED);
    }

    public boolean isPastInvoiceReceived() {
        return getLastAcquisitionProcessState().isPastInvoiceReceived();
    }

    public Unit getUnit() {
        return getRequestingUnit();
    }

    public Money getAmountAllocatedToUnit(Unit unit) {
        return getAcquisitionRequest().getAmountAllocatedToUnit(unit);
    }

    public Money getAcquisitionRequestValueLimit() {
        return null;
    }

    public Unit getRequestingUnit() {
        return getAcquisitionRequest().getRequestingUnit();
    }

    public boolean isAllowedToViewCostCenterExpenditures() {
        try {
            return (getUnit() != null && isResponsibleForUnit()) || ExpenditureTrackingSystem.isAccountingManagerGroupMember()
                    || ExpenditureTrackingSystem.isProjectAccountingManagerGroupMember() || isAccountingEmployee()
                    || isProjectAccountingEmployee() || ExpenditureTrackingSystem.isManager();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }

    public boolean isAllowedToViewSupplierExpenditures() {
        return ExpenditureTrackingSystem.isAcquisitionCentralGroupMember()
                || ExpenditureTrackingSystem.isAcquisitionCentralManagerGroupMember() || ExpenditureTrackingSystem.isManager();
    }

    public boolean checkRealValues() {
        return getAcquisitionRequest().checkRealValues();
    }

    public Integer getYear() {
        return getPaymentProcessYear().getYear();
    }

    /*
     * use getProcessNumber() instead
     */
    @Override
    @Deprecated
    public String getAcquisitionProcessId() {
        return getProcessNumber();
    }

    public boolean isProcessFlowCharAvailable() {
        return false;
    }

    public List<AcquisitionProcessStateType> getAvailableStates() {
        return Collections.emptyList();
    }

    public String getAllocationIds() {
        final Stream<Financer> financers = getAcquisitionRequest().getFinancersSet().stream();
        return financers.map(f -> f.getFundAllocationIds())
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }

    public String getEffectiveAllocationIds() {
        final Stream<Financer> financers = getAcquisitionRequest().getFinancersSet().stream();
        return financers.map(f -> f.getEffectiveFundAllocationIds())
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }

    @Override
    public AcquisitionRequest getRequest() {
        return getAcquisitionRequest();
    }

    @Override
    public boolean isInGenesis() {
        return getAcquisitionProcessState().isInGenesis();
    }

    @Override
    public boolean isInApprovedState() {
        return getAcquisitionProcessState().isInApprovedState();
    }

    @Override
    public boolean isPendingFundAllocation() {
        return getAcquisitionProcessState().isInAllocatedToSupplierState();
    }

    @Override
    public boolean isInAuthorizedState() {
        return getAcquisitionProcessState().isAuthorized();
    }

    @Override
    public boolean isInvoiceConfirmed() {
        return getAcquisitionProcessState().isInvoiceConfirmed();
    }

    @Override
    public boolean isAllocatedPermanently() {
        return getAcquisitionProcessState().isAllocatedPermanently();
    }

    @Override
    public Collection<Supplier> getSuppliers() {
        return getRequest().getSuppliers();

    }

    @Override
    public String getProcessStateName() {
        return getLastAcquisitionProcessState().getLocalizedName();
    }

    @Override
    public int getProcessStateOrder() {
        return getLastAcquisitionProcessState().getAcquisitionProcessStateType().ordinal();
    }

    public Boolean getShouldSkipSupplierFundAllocation() {
        return getSkipSupplierFundAllocation();
    }

    public String getAcquisitionRequestDocumentID() {
        return hasPurchaseOrderDocument() ? getPurchaseOrderDocument().getRequestId() : ExpenditureTrackingSystem.getInstance()
                .nextAcquisitionRequestDocumentID();
    }

    // TODO: delete this method... it's not used.
    @Deprecated
    public AcquisitionProposalDocument getAcquisitionProposalDocument() {
        return (AcquisitionProposalDocument) getFileStream(AcquisitionProposalDocument.class).findAny().orElse(null);
    }

    public boolean hasAcquisitionProposalDocument() {
        return getFileStream(AcquisitionProposalDocument.class).findAny().isPresent();
    }

    public void setPurchaseOrderDocument(PurchaseOrderDocument document) {
        addFiles(document);
    }

    public PurchaseOrderDocument getPurchaseOrderDocument() {
        final Stream<ProcessFile> stream = getFileStream(PurchaseOrderDocument.class);
        return (PurchaseOrderDocument) stream.filter(f -> f.getProcessWithDeleteFile() == null)
            .findAny().orElse(null);
    }

    public boolean hasPurchaseOrderDocument() {
        return getFileStream(PurchaseOrderDocument.class).findAny().isPresent();
    }
    
    public AdvancePaymentDocument getAdvancePaymentDocument() {
        final Stream<ProcessFile> stream = getFileStream(AdvancePaymentDocument.class);
        return (AdvancePaymentDocument) stream.filter(f -> f.getProcessWithDeleteFile() == null).findAny().orElse(null);
    }

    public boolean hasAdvancePaymentDocument() {
        return getAdvancePaymentDocument() != null;
    }
    
    public AdvancePaymentDocument getSignedAdvancePaymentDocument() {
        AdvancePaymentDocument advancePaymentDocument = getAdvancePaymentDocument();
        return advancePaymentDocument != null && advancePaymentDocument.isSigned() ? advancePaymentDocument : null;
    }

    @Override
    public boolean isCanceled() {
        return getLastAcquisitionProcessState().isCanceled() || getLastAcquisitionProcessState().isRejected();
    }

    @Override
    public void revertToState(ProcessState processState) {
        final AcquisitionProcessState acquisitionProcessState = (AcquisitionProcessState) processState;
        final AcquisitionProcessStateType acquisitionProcessStateType = acquisitionProcessState.getAcquisitionProcessStateType();
        if (acquisitionProcessStateType != null && acquisitionProcessStateType != AcquisitionProcessStateType.CANCELED) {
            new AcquisitionProcessState(this, acquisitionProcessStateType);
        }
    }

    @Deprecated
    public boolean hasFundAllocationExpirationDate() {
        return getFundAllocationExpirationDate() != null;
    }

    @Deprecated
    public boolean hasAcquisitionRequest() {
        return getAcquisitionRequest() != null;
    }

}
