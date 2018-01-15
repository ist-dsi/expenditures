/*
 * @(#)RefundProcess.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiPredicate;
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import module.finance.util.Money;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.GiveProcess;
import module.workflow.activities.ReleaseProcess;
import module.workflow.activities.StealProcess;
import module.workflow.activities.TakeProcess;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.ClassNameBundle;
import module.workflow.util.PresentableProcessState;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionItemClassification;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.AllocateFundsPermanently;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.AllocateProjectFundsPermanently;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.Approve;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.Authorize;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.FundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.GenericAddPayingUnit;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.GenericAssignPayingUnitToItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.GenericRemovePayingUnit;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.ProjectFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.RemoveCancelProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.RemoveFundsPermanentlyAllocated;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.RemovePermanentProjectFunds;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.UnApprove;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.UnAuthorize;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.CancelRefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.ChangeFinancersAccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.ChangeProcessRequester;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.ChangeRefundItemClassification;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.ConfirmInvoices;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.CreateRefundInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.CreateRefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.CreateRefundItemWithMaterial;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.DeleteRefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.DistributeRealValuesForPayingUnits;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.EditRefundInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.EditRefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.EditRefundItemWithMaterial;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.RefundPerson;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.RemoveFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.RemoveProjectFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.RemoveRefundInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.RevertInvoiceConfirmationSubmition;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.SetSkipSupplierFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.SubmitForApproval;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.SubmitForInvoiceConfirmation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.UnSubmitForApproval;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.UnSubmitForFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.UnconfirmInvoices;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.UnsetSkipSupplierFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.Util;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateRefundProcessBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;
import pt.ist.fenixframework.Atomic;

@ClassNameBundle(bundle = "ExpenditureResources")
/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * @author Luis Cruz
 * @author João Alfaiate
 * 
 */
public class RefundProcess extends RefundProcess_Base {

    private static List<WorkflowActivity<RefundProcess, ? extends ActivityInformation<RefundProcess>>> activities =
            new ArrayList<WorkflowActivity<RefundProcess, ? extends ActivityInformation<RefundProcess>>>();

    static {
        activities.add(new Approve<RefundProcess>());
        activities.add(new UnApprove<RefundProcess>());
        activities.add(new CancelRefundProcess());
        activities.add(new ConfirmInvoices());
        activities.add(new RemoveFundAllocation());
        activities.add(new RemoveProjectFundAllocation());
        activities.add(new RevertInvoiceConfirmationSubmition());
        activities.add(new SetSkipSupplierFundAllocation());
        activities.add(new SubmitForApproval());
        activities.add(new SubmitForInvoiceConfirmation());
        activities.add(new UnconfirmInvoices());
        activities.add(new UnsetSkipSupplierFundAllocation());
        activities.add(new UnSubmitForApproval());
        activities.add(new UnSubmitForFundAllocation());
        activities.add(new CreateRefundItem());
        activities.add(new CreateRefundItemWithMaterial());
        activities.add(new EditRefundItem());
        activities.add(new EditRefundItemWithMaterial());
        activities.add(new DeleteRefundItem());
        activities.add(new CreateRefundInvoice());
        activities.add(new RemoveRefundInvoice());
        activities.add(new GenericAddPayingUnit<RefundProcess>());
        activities.add(new GenericRemovePayingUnit<RefundProcess>());
        activities.add(new Authorize<RefundProcess>());
        activities.add(new UnAuthorize<RefundProcess>());
        activities.add(new GenericAssignPayingUnitToItem<RefundProcess>());
        activities.add(new DistributeRealValuesForPayingUnits());
        activities.add(new FundAllocation<RefundProcess>());
        activities.add(new ProjectFundAllocation<RefundProcess>());
        activities.add(new RemoveFundsPermanentlyAllocated<RefundProcess>());
        activities.add(new RemovePermanentProjectFunds<RefundProcess>());
        activities.add(new TakeProcess<RefundProcess>());
        activities.add(new GiveProcess<RefundProcess>());
        activities.add(new ReleaseProcess<RefundProcess>());
        activities.add(new StealProcess<RefundProcess>());
        // activities.add(new GiveProcess<RefundProcess>());
        activities.add(new EditRefundInvoice());
        activities.add(new AllocateProjectFundsPermanently<RefundProcess>());
        activities.add(new AllocateFundsPermanently<RefundProcess>());
        activities.add(new ChangeFinancersAccountingUnit());
        activities.add(new ChangeProcessRequester());
        activities.add(new ChangeRefundItemClassification());
        activities.add(new RemoveCancelProcess<RefundProcess>());
        activities.add(new RefundPerson());
//	activities.add(new MarkProcessAsCCPProcess());
//	activities.add(new UnmarkProcessAsCCPProcess());
    }

    public static void registerActivity(WorkflowActivity<RefundProcess, ? extends ActivityInformation<RefundProcess>> activity) {
        activities.add(activity);
    }

    public static void registerActivityPredicate(final Class clazz, final BiPredicate predicate) {
        for (final WorkflowActivity activity : activities) {
            if (activity.getClass().equals(clazz)) {
                activity.registerIsActivePredicate(predicate);
            }
        }
    }

    public RefundProcess(Person requestor, String refundeeName, String refundeeFiscalCode, Unit requestingUnit) {
        super();
        new RefundRequest(this, requestor, refundeeName, refundeeFiscalCode, requestingUnit);
        new RefundProcessState(this, RefundProcessStateType.IN_GENESIS);
        setSkipSupplierFundAllocation(Boolean.FALSE);
        setProcessNumber(constructProcessNumber());
    }

    public RefundProcess(Person requestor, Person refundee, Unit requestingUnit) {
        super();
        new RefundRequest(this, requestor, refundee, requestingUnit);
        new RefundProcessState(this, RefundProcessStateType.IN_GENESIS);
        setSkipSupplierFundAllocation(Boolean.FALSE);
        setProcessNumber(constructProcessNumber());
    }

    protected String constructProcessNumber() {
        final ExpenditureTrackingSystem instance = getExpenditureTrackingSystem();
        if (instance.hasProcessPrefix()) {
            return instance.getInstitutionalProcessNumberPrefix() + "/" + getYear() + "/" + getAcquisitionProcessNumber();
        }
        return getYear() + "/" + getAcquisitionProcessNumber();
    }

    @Atomic
    public static RefundProcess createNewRefundProcess(CreateRefundProcessBean bean) {

        final RefundProcess process = bean.isExternalPerson() ? new RefundProcess(bean.getRequestor(), bean.getRefundeeName(),
                bean.getRefundeeFiscalCode(),
                bean.getRequestingUnit()) : new RefundProcess(bean.getRequestor(), bean.getRefundee(), bean.getRequestingUnit());

        process.setUnderCCPRegime(bean.isUnderCCP());

        if (bean.isRequestUnitPayingUnit()) {
            process.getRequest().addPayingUnit(bean.getRequestingUnit());
        }
        if (bean.isForMission()) {
            if (bean.getMissionProcess() == null) {
                throw new DomainException(Bundle.EXPENDITURE, "mission.process.is.mandatory");
            }
            process.setMissionProcess(bean.getMissionProcess());
        }

        return process;
    }

    protected RefundProcessState getLastProcessState() {
        return (RefundProcessState) Collections.max(getProcessStates(), ProcessState.COMPARATOR_BY_WHEN);
    }

    public RefundProcessState getProcessState() {
        return getLastProcessState();
    }

    @Override
    public boolean isInGenesis() {
        return getProcessState().isInGenesis();
    }

    @Override
    public Person getRequestor() {
        return getRequest().getRequester();
    }

    @Override
    public void submitForApproval() {
        new RefundProcessState(this, RefundProcessStateType.SUBMITTED_FOR_APPROVAL);
    }

    public void unSubmitForApproval() {
        final RefundProcessState refundProcessState = getProcessState();
        refundProcessState.setRefundProcessStateType(RefundProcessStateType.IN_GENESIS);
    }

    @Override
    public boolean isPendingApproval() {
        final RefundProcessState refundProcessState = getProcessState();
        return refundProcessState.isPendingApproval();
    }

    @Override
    public void submitForFundAllocation() {
        createFundAllocationRequest(false);
        new RefundProcessState(this, RefundProcessStateType.APPROVED);
    }

    public void createFundAllocationRequest(final boolean isFinalFundAllocation) {
        final RefundRequest refundRequest = getRequest();
        refundRequest.createFundAllocationRequest(isFinalFundAllocation);
    }

    @Override
    public boolean isInApprovedState() {
        return getProcessState().isInApprovedState();
    }

    @Override
    public boolean isInAllocatedToUnitState() {
        return getProcessState().isInAllocatedToUnitState();
    }

    @Override
    protected void authorize() {
        new RefundProcessState(this, RefundProcessStateType.AUTHORIZED);
    }

    @Override
    public boolean isPendingFundAllocation() {
        return isInApprovedState();
    }

    @Override
    public void allocateFundsToUnit() {
        new RefundProcessState(this, RefundProcessStateType.FUNDS_ALLOCATED);
    }

    @Override
    public boolean isInAuthorizedState() {
        return getProcessState().isAuthorized();
    }

    public void unApproveByAll() {
        getRequest().unSubmitForFundsAllocation();
    }

    public boolean isInSubmittedForInvoiceConfirmationState() {
        return getProcessState().isInSubmittedForInvoiceConfirmationState();
    }

    public List<RefundableInvoiceFile> getRefundableInvoices() {
        final List<RefundableInvoiceFile> invoices = new ArrayList<RefundableInvoiceFile>();
        for (final RequestItem item : getRequest().getRequestItems()) {
            invoices.addAll(((RefundItem) item).getRefundableInvoices());
        }
        return invoices;
    }

    public void confirmInvoicesByPerson(Person person) {
        for (final RequestItem item : getRequest().getRequestItems()) {
            item.confirmInvoiceBy(person);
        }

        if (getRequest().isConfirmedForAllInvoices()) {
            createFundAllocationRequest(true);
            confirmInvoices();
        }
    }

    public void unconfirmInvoicesByPerson(Person person) {
        for (final RequestItem item : getRequest().getRequestItems()) {
            item.unconfirmInvoiceBy(person);
        }
        submitForInvoiceConfirmation();
    }

    public void revertInvoiceConfirmationSubmition() {
        new RefundProcessState(this, RefundProcessStateType.AUTHORIZED);
    }

    public void submitForInvoiceConfirmation() {
        new RefundProcessState(this, RefundProcessStateType.SUBMITTED_FOR_INVOICE_CONFIRMATION);
    }

    public void confirmInvoices() {
        new RefundProcessState(this, RefundProcessStateType.INVOICES_CONFIRMED);
    }

    public boolean isPendingInvoicesConfirmation() {
        return getProcessState().isPendingInvoicesConfirmation();
    }

    @Override
    public boolean isActive() {
        return getProcessState().isActive();
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

    @Override
    public boolean isInvoiceConfirmed() {
        return getProcessState().isInvoiceConfirmed();
    }

    @Override
    public void allocateFundsPermanently() {
        new RefundProcessState(this, RefundProcessStateType.FUNDS_ALLOCATED_PERMANENTLY);
    }

    @Override
    public boolean isAllocatedPermanently() {
        return getProcessState().isAllocatedPermanently();
    }

    @Override
    public void resetEffectiveFundAllocationId() {
        getRequest().resetEffectiveFundAllocationId();
        confirmInvoice();
    }

    protected void confirmInvoice() {
        new RefundProcessState(this, RefundProcessStateType.INVOICES_CONFIRMED);
    }

    public boolean hasFundsAllocatedPermanently() {
        return getProcessState().hasFundsAllocatedPermanently();
    }

    public void refundPerson(final String paymentReference) {
        getRequest().setPaymentReference(paymentReference);
        new RefundProcessState(this, RefundProcessStateType.REFUNDED);
    }

    @Override
    public boolean isPayed() {
        return getRequest().isPayed();
    }

    public boolean isAnyRefundInvoiceAvailable() {
        for (final RefundItem item : getRequest().getRefundItemsSet()) {
            if (!item.getRefundableInvoices().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAccessible(User user) {
        return isAvailableForPerson(user.getExpenditurePerson());
    }

    public boolean isAvailableForCurrentUser() {
        final Person loggedPerson = Person.getLoggedPerson();
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
                || getRequest().getRequestingUnit().isResponsible(person) || isResponsibleForAtLeastOnePayingUnit(person)
                || isAccountingEmployee(person) || isProjectAccountingEmployee(person) || isTreasuryMember(person)
                || isObserver(person);
    }

    @Override
    public boolean isAuthorized() {
        return super.isAuthorized() && getRefundableInvoices().isEmpty();
    }

    @Override
    public boolean isCanceled() {
        return getProcessState().isCanceled();
    }

    @Override
    public boolean isRefundProcess() {
        return true;
    }

    public void cancel() {
        getRequest().cancel();
        new RefundProcessState(this, RefundProcessStateType.CANCELED);
    }

    @Override
    public String getProcessStateDescription() {
        return getLastProcessState().getLocalizedName();
    }

    @Override
    public Set<Supplier> getSuppliers() {
        final Set<Supplier> suppliers = new HashSet<Supplier>();
        for (final RefundableInvoiceFile invoice : getRefundableInvoices()) {
            final Supplier supplier = invoice.getSupplier();
            if (supplier != null) {
                suppliers.add(supplier);
            }
        }
        return suppliers;
    }

    @Override
    public boolean isAppiableForYear(final int year) {
        return Util.isAppiableForYear(year, this);
    }

    @Override
    public String getProcessStateName() {
        return getProcessState().getLocalizedName();
    }

    @Override
    public int getProcessStateOrder() {
        return getProcessState().getRefundProcessStateType().ordinal();
    }

    @Override
    public <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> List<T> getActivities() {
        return (List<T>) activities;
    }

    @Override
    public <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> Stream<T> getActivityStream() {
        final List activities = this.activities;
        return activities.stream();
    }

    @Override
    public void revertToState(ProcessState processState) {
        final RefundProcessState refundProcessState = (RefundProcessState) processState;
        final RefundProcessStateType refundProcessStateType = refundProcessState.getRefundProcessStateType();
        if (refundProcessStateType != null && refundProcessStateType != RefundProcessStateType.CANCELED) {
            new RefundProcessState(this, refundProcessStateType);
        }
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/AcquisitionResources", "label.RefundProcess");
    }

    public Boolean getShouldSkipSupplierFundAllocation() {
        return !getUnderCCPRegime() || super.getSkipSupplierFundAllocation();
    }

    @Override
    public PresentableProcessState getPresentableAcquisitionProcessState() {
        return getProcessState().getRefundProcessStateType();
    }

    @Override
    public List<? extends PresentableProcessState> getAvailablePresentableStates() {
        return Arrays.asList(RefundProcessStateType.values());
    }

    @Override
    public List<Class<? extends ProcessFile>> getAvailableFileTypes() {
        final List<Class<? extends ProcessFile>> availableFileTypes = new ArrayList<Class<? extends ProcessFile>>();
        availableFileTypes.add(RefundableInvoiceFile.class);
        availableFileTypes.addAll(super.getAvailableFileTypes());
        return availableFileTypes;
    }

    @Override
    public List<Class<? extends ProcessFile>> getUploadableFileTypes() {
        final List<Class<? extends ProcessFile>> uploadableFileTypes = super.getUploadableFileTypes();
        uploadableFileTypes.remove(RefundableInvoiceFile.class);
        return uploadableFileTypes;
    }

    @Override
    public Money getTotalValue() {
        return getRequest().getCurrentTotalValue();
    }

    @Override
    public Set<CPVReference> getCPVReferences() {
        final RefundRequest request = getRequest();
        return request.getCPVReferences();
    }

    public void checkIsFundAllocationAllowed() {
        final Map<Supplier, Map<CPVReference, Money>> allocationMap = new HashMap<Supplier, Map<CPVReference, Money>>();

        for (final RefundItem refundItem : getRequest().getRefundItemsSet()) {
            final CPVReference cpvReference = refundItem.getCPVReference();

            for (final RefundableInvoiceFile refundInvoice : refundItem.getRefundableInvoices()) {
                final Supplier supplier = refundInvoice.getSupplier();
                if (supplier != null) {
                    final String key = cpvReference.getExternalId() + supplier.getExternalId();
                    final Money refundableValue = refundInvoice.getRefundableValue();

                    if (!allocationMap.containsKey(supplier)) {
                        allocationMap.put(supplier, new HashMap<CPVReference, Money>());
                    }
                    final Map<CPVReference, Money> map = allocationMap.get(supplier);
                    if (map.containsKey(cpvReference)) {
                        map.put(cpvReference, refundableValue.add(map.get(cpvReference)));
                    } else {
                        map.put(cpvReference, refundableValue);
                    }
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
                    throw new DomainException(Bundle.ACQUISITION,
                            "acquisitionProcess.message.exception.SupplierDoesNotAlloweAmount");
                }
                total = total.add(value);
            }
            if (!checkSupplierLimitsByCPV && !supplier.isFundAllocationAllowed(total)) {
                throw new DomainException(Bundle.ACQUISITION, "acquisitionProcess.message.exception.SupplierDoesNotAlloweAmount");
            }
        }
    }

    @Override
    public AcquisitionItemClassification getGoodsOrServiceClassification() {
        final RefundRequest request = getRequest();
        return request.getGoodsOrServiceClassification();
    }

    @Deprecated
    public boolean hasUnderCCPRegime() {
        return getUnderCCPRegime() != null;
    }

    @Deprecated
    public boolean hasRequest() {
        return getRequest() != null;
    }

}
