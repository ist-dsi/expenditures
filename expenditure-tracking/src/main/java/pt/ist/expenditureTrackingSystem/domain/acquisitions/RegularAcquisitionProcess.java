/*
 * @(#)RegularAcquisitionProcess.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem._development.ExternalIntegration;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.FundAllocationExpirationDate.FundAllocationNotAllowedException;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public abstract class RegularAcquisitionProcess extends RegularAcquisitionProcess_Base {

    public enum ActivityScope {
        REQUEST_INFORMATION, REQUEST_ITEM;
    }

    public RegularAcquisitionProcess() {
        super();
    }

    public boolean isSimplifiedAcquisitionProcess() {
        return false;
    }

    public boolean isStandardAcquisitionProcess() {
        return false;
    }

    public Set<AcquisitionInvoice> getConfirmedInvoices(Person person) {
        Set<AcquisitionInvoice> invoices = new HashSet<AcquisitionInvoice>();
        for (AcquisitionRequestItem item : getAcquisitionRequest().getAcquisitionRequestItemsSet()) {
            invoices.addAll(item.getConfirmedInvoices(person));
        }
        return invoices;
    }

    public Set<AcquisitionInvoice> getUnconfirmedInvoices(Person person) {
        Set<AcquisitionInvoice> invoices = new HashSet<AcquisitionInvoice>();
        for (AcquisitionRequestItem item : getAcquisitionRequest().getAcquisitionRequestItemsSet()) {
            if (item.isResponsible(person)) {
                invoices.addAll(item.getUnconfirmedInvoices(person));
            }
        }
        return invoices;
    }

    public Set<AcquisitionInvoice> getAllUnconfirmedInvoices() {
        Set<AcquisitionInvoice> invoices = new HashSet<AcquisitionInvoice>();
        for (AcquisitionRequestItem item : getAcquisitionRequest().getAcquisitionRequestItemsSet()) {
            invoices.addAll(item.getAllUnconfirmedInvoices());
        }
        return invoices;
    }

    public void confirmInvoiceBy(Person person) {
        getAcquisitionRequest().confirmInvoiceFor(person);
        if (getAcquisitionRequest().isInvoiceConfirmedBy() && getLastAcquisitionProcessState().isPendingInvoiceConfirmation()) {
            confirmInvoice();
        }
    }

    public void cancelInvoiceConfirmationBy(final Person person) {
        getAcquisitionRequest().unconfirmInvoiceFor(person);
        if (getLastAcquisitionProcessState().isInvoiceConfirmed()) {
            cancelInvoiceConfirmation();
        }
    }

    public void unconfirmInvoiceForAll() {
        getAcquisitionRequest().unconfirmInvoiceForAll();
        cancelInvoiceConfirmation();
    }

    @Override
    public void allocateFundsPermanently() {
        new AcquisitionProcessState(this, AcquisitionProcessStateType.FUNDS_ALLOCATED_PERMANENTLY);
    }

    public void cancel() {
        new AcquisitionProcessState(this, AcquisitionProcessStateType.CANCELED);
    }

    protected void authorize() {
        final AcquisitionProcessStateType type;
        if (ExpenditureTrackingSystem.isInvoiceAllowedToStartAcquisitionProcess() && hasInvoiceFile()) {
            type = AcquisitionProcessStateType.INVOICE_RECEIVED;
            getRequest().processReceivedInvoice();
        } else {
            type = AcquisitionProcessStateType.AUTHORIZED;
        }
        new AcquisitionProcessState(this, type);
    }

    protected void cancelInvoiceConfirmation() {
        new AcquisitionProcessState(this, AcquisitionProcessStateType.SUBMITTED_FOR_CONFIRM_INVOICE);
    }

    protected void confirmInvoice() {
        new AcquisitionProcessState(this, AcquisitionProcessStateType.INVOICE_CONFIRMED);
    }

    public void allocateFundsToUnit() {
        new AcquisitionProcessState(this, AcquisitionProcessStateType.FUNDS_ALLOCATED);
    }

    public void allocateFundsToSupplier() {
        new AcquisitionProcessState(this, AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER);
    }

    public void acquisitionPayed() {
        new AcquisitionProcessState(this, AcquisitionProcessStateType.ACQUISITION_PAYED);
    }

    public void invoiceReceived() {
        new AcquisitionProcessState(this, AcquisitionProcessStateType.INVOICE_RECEIVED);
    }

    public void reject() {
        new AcquisitionProcessState(this, AcquisitionProcessStateType.REJECTED);
    }

    public void inGenesis() {
        new AcquisitionProcessState(this, AcquisitionProcessStateType.IN_GENESIS);
    }

    @Override
    public void submitForApproval() {
        new AcquisitionProcessState(this, AcquisitionProcessStateType.SUBMITTED_FOR_APPROVAL);
    }

    public void processAcquisition() {
        new AcquisitionProcessState(this, AcquisitionProcessStateType.ACQUISITION_PROCESSED);
    }

    public void revertProcessedAcquisition() {
        new AcquisitionProcessState(this, AcquisitionProcessStateType.AUTHORIZED);
    }

    public void submitedForInvoiceConfirmation() {
        new AcquisitionProcessState(this, AcquisitionProcessStateType.SUBMITTED_FOR_CONFIRM_INVOICE);
        if (getAllUnconfirmedInvoices().isEmpty()) {
            confirmInvoice();
        }
    }

    public void submitForFundAllocation() {
        final AcquisitionProcessStateType type;
        if (ExpenditureTrackingSystem.isInvoiceAllowedToStartAcquisitionProcess() && hasInvoiceFile()) {
            type = AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER;
            if (ExternalIntegration.isActive()) {
                createFundAllocationRequest(false);
            }
        } else {
            type = AcquisitionProcessStateType.SUBMITTED_FOR_FUNDS_ALLOCATION;
        }
        new AcquisitionProcessState(this, type);
    }

    public void skipFundAllocation() {
        new AcquisitionProcessState(this, AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER);
    }

    @Override
    public void resetEffectiveFundAllocationId() {
        getAcquisitionRequest().resetEffectiveFundAllocationId();
        confirmInvoice();
    }

    @Override
    public boolean isProcessFlowCharAvailable() {
        return true;
    }

    @Override
    public void setSkipSupplierFundAllocation(Boolean skipSupplierFundAllocation) {
        throw new DomainException("error.illegal.method.use");
    }

    public void unSkipSupplierFundAllocation() {
        checkSupplierLimit();
        super.setSkipSupplierFundAllocation(Boolean.FALSE);
        if (!getAcquisitionProcessState().isInGenesis()) {
            LocalDate now = new LocalDate();
            setFundAllocationExpirationDate(now.plusDays(90));
        }
    }

    public void skipSupplierFundAllocation() {
        super.setSkipSupplierFundAllocation(Boolean.TRUE);
        setFundAllocationExpirationDate(null);
    }

    public boolean isFinanceByAnyUnit(List<Unit> fromUnits) {
        for (Financer financer : getAcquisitionRequest().getFinancers()) {
            if (fromUnits.contains(financer.getUnit())) {
                return true;
            }
        }
        return false;
    }

    public boolean isPersonAbleToDirectlyAuthorize(Person person) {
        return isFinanceByAnyUnit(person.getDirectResponsibleUnits()) ? true : getRequestingUnit().isMostDirectAuthorization(
                person, getAcquisitionRequest().getTotalItemValueWithAdditionalCostsAndVat());
    }

    public List<Unit> getFinancingUnits() {
        List<Unit> units = new ArrayList<Unit>();
        for (Financer financer : getAcquisitionRequest().getFinancers()) {
            units.add(financer.getUnit());
        }
        return units;
    }

    public boolean isInAuthorizedState() {
        return getAcquisitionProcessState().isAuthorized();
    }

    public boolean isPendingInvoiceConfirmation() {
        return getAcquisitionProcessState().isPendingInvoiceConfirmation();
    }

    public void removeFundAllocationExpirationDate() {
        setFundAllocationExpirationDate(null);
        if (!getAcquisitionProcessState().isCanceled()) {
            submitForFundAllocation();
        }
    }

    @Override
    public boolean isInAllocatedToUnitState() {
        return getAcquisitionProcessState().isInAllocatedToUnitState();
    }

    @Override
    public Money getTotalValue() {
        return getAcquisitionRequest().getCurrentTotalValue();
    }

    @Override
    public Set<CPVReference> getCPVReferences() {
        final AcquisitionRequest acquisitionRequest = getRequest();
        return acquisitionRequest.getCPVReferences();
    }

    public void createFundAllocationRequest(final boolean isFinalFundAllocation) {
        final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
        acquisitionRequest.createFundAllocationRequest(isFinalFundAllocation);
    }

    public void cancelFundAllocationRequest(final boolean isFinalFundAllocation) {
        final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
        acquisitionRequest.cancelFundAllocationRequest(isFinalFundAllocation);
    }

    public boolean hasInvoiceFile() {
        return false;
    }

    public boolean isCommitted() {
        final ExpenditureTrackingSystem instance = ExpenditureTrackingSystem.getInstance();
        if (instance.getRequireCommitmentNumber() != null && instance.getRequireCommitmentNumber().booleanValue()) {
            final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
            return acquisitionRequest.isCommitted();
        }
        return true;
    }

    public boolean isPendingCommitmentByUser(final User user) {
        final ExpenditureTrackingSystem instance = ExpenditureTrackingSystem.getInstance();
        if (instance.getRequireCommitmentNumber() != null && instance.getRequireCommitmentNumber().booleanValue()) {
            final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
            return acquisitionRequest.isPendingCommitmentByUser(user);
        }
        return false;
    }

    public boolean hasCommitmentByUser(final User user) {
        final ExpenditureTrackingSystem instance = ExpenditureTrackingSystem.getInstance();
        if (instance.getRequireCommitmentNumber() != null && instance.getRequireCommitmentNumber().booleanValue()) {
            final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
            return acquisitionRequest.hasCommitmentByUser(user);
        }
        return false;
    }

    public void checkSupplierLimit() {
        final Map<Supplier, Map<CPVReference, Money>> allocationMap = new HashMap<Supplier, Map<CPVReference, Money>>();

        for (final RequestItem requestItem : getRequest().getRequestItemsSet()) {
            final AcquisitionRequestItem acquisitionRequestItem = (AcquisitionRequestItem) requestItem;
            final CPVReference cpvReference = requestItem.getCPVReference();
            final Money value = acquisitionRequestItem.getCurrentSupplierAllocationValue();

            for (final Supplier supplier : getSuppliers()) {
                final String key = cpvReference.getExternalId() + supplier.getExternalId();

                if (!allocationMap.containsKey(supplier)) {
                    allocationMap.put(supplier, new HashMap<CPVReference, Money>());
                }
                final Map<CPVReference, Money> map = allocationMap.get(supplier);
                if (map.containsKey(cpvReference)) {
                    map.put(cpvReference, value.add(map.get(cpvReference)));
                } else {
                    map.put(cpvReference, value);
                }
            }
        }

        final boolean checkSupplierLimitsByCPV = ExpenditureTrackingSystem.getInstance().checkSupplierLimitsByCPV();

        for (final Entry<Supplier, Map<CPVReference, Money>> entry : allocationMap.entrySet()) {
            final Supplier supplier = entry.getKey();
            final Map<CPVReference, Money> map = entry.getValue();

            Money total = Money.ZERO;
            for (final Entry<CPVReference, Money> centry : map.entrySet()) {
                final CPVReference cpvReference = centry.getKey();
                final Money value = centry.getValue();
                if (checkSupplierLimitsByCPV && !supplier.isFundAllocationAllowed(cpvReference.getCode(), value)) {
                    throw new FundAllocationNotAllowedException();
                }
                total = total.add(value);
            }
            if (!checkSupplierLimitsByCPV && !supplier.isFundAllocationAllowed(total)) {
                throw new FundAllocationNotAllowedException();
            }
        }
    }

}
