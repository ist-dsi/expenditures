/*
 * @(#)SimplifiedProcedureProcess.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import module.finance.util.Money;
import module.mission.domain.MissionSystem;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.AddObserver;
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
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionInvoiceState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionItemClassification;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProposalDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CreditNoteDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PurchaseOrderDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.AllocateFundsPermanently;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.AllocateProjectFundsPermanently;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.Authorize;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.CommitFunds;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.DeleteCommitmentNumber;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.FundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.GenericAddPayingUnit;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.GenericAssignPayingUnitToItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.GenericRemovePayingUnit;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.ProjectFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.RemoveFundsPermanentlyAllocated;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.RemovePermanentProjectFunds;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.ReverifiedAfterCommitment;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.UnApprove;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.UnAuthorize;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.CancelAcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.CancelInvoiceConfirmation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.ChangeAcquisitionRequestItemClassification;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.ChangeAcquisitionRequestItemMaterial;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.ChangeFinancersAccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.ChangeProcessClassification;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.ConfirmInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.CreateAcquisitionPurchaseOrderDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.CreateAcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.CreateAcquisitionRequestItemWithMaterial;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.DeleteAcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.DistributeRealValuesForPayingUnits;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.EditAcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.EditAcquisitionRequestItemRealValues;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.EditAcquisitionRequestItemWithMaterial;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.EditSimpleContractDescription;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.ExceptionalChangeRequestingPerson;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.FundAllocationExpirationDate;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.FundAllocationExpirationDateAndPurchaseOrderDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.JumpToProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.LockInvoiceReceiving;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.PayAcquisition;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RejectAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RemoveCancelProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RemoveFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RemoveFundAllocationExpirationDate;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RemoveFundAllocationExpirationDateForResponsible;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RemoveProjectFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RevertInvoiceSubmission;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RevertProcessNotConfirmmingFundAllocationExpirationDate;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RevertSkipPurchaseOrderDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RevertToInvoiceConfirmation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SelectSupplier;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SendPurchaseOrderToSupplier;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SetSkipSupplierFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SkipPurchaseOrderDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SubmitForApproval;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SubmitForConfirmInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SubmitForFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.UnSubmitForApproval;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.UnlockInvoiceReceiving;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.UnsetSkipSupplierFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.announcements.RCISTAnnouncement;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.ist.fenixframework.Atomic;

@ClassNameBundle(bundle = "ExpenditureResources")
/**
 * 
 * @author Diogo Figueiredo
 * @author João Neves
 * @author João Antunes
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class SimplifiedProcedureProcess extends SimplifiedProcedureProcess_Base {

    public static enum ProcessClassification implements IPresentableEnum {

        CCP(new Money("5000"), true, "RS 5000"), CT10000(new Money("10000"), "CT 10000"),
        CT75000(new Money("75000"), "CT 75000"), NORMAL(new Money("75000"), "NORMAL");

        final private Money value;
        final private String shortDescription;
        final private boolean ccp;

        ProcessClassification(Money value, String shortDescription) {
            this(value, false, shortDescription);
        }

        ProcessClassification(Money value, boolean ccp, String shortDescription) {
            this.value = value;
            this.ccp = ccp;
            this.shortDescription = shortDescription;
        }

        public Money getLimit() {
            return value;
        }

        public boolean isCCP() {
            return ccp;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        @Override
        public String getLocalizedName() {
            return BundleUtil.getString("resources/ExpenditureResources", "label.processClassification." + name());
        }
    }

    private static List<AcquisitionProcessStateType> availableStates = new ArrayList<AcquisitionProcessStateType>();

    private static List<WorkflowActivity<? extends RegularAcquisitionProcess, ? extends ActivityInformation<? extends RegularAcquisitionProcess>>> activities =
            new ArrayList<WorkflowActivity<? extends RegularAcquisitionProcess, ? extends ActivityInformation<? extends RegularAcquisitionProcess>>>();

    static {
        activities.add(new CommitFunds());
        activities.add(new DeleteCommitmentNumber());
        activities.add(new ReverifiedAfterCommitment());
        activities.add(new CreateAcquisitionPurchaseOrderDocument());
        activities.add(new SendPurchaseOrderToSupplier());
        activities.add(new SkipPurchaseOrderDocument());
        activities.add(new RevertSkipPurchaseOrderDocument());
        activities.add(new GenericAddPayingUnit<RegularAcquisitionProcess>());
        activities.add(new GenericRemovePayingUnit<RegularAcquisitionProcess>());
        activities.add(new SubmitForApproval());
        activities.add(new SubmitForFundAllocation());
        activities.add(new RejectAcquisitionProcess());
        activities.add(new UnApprove<RegularAcquisitionProcess>());
        activities.add(new FundAllocationExpirationDate());
        activities.add(new FundAllocationExpirationDateAndPurchaseOrderDocument());
        activities.add(new RevertProcessNotConfirmmingFundAllocationExpirationDate());
        activities.add(new RevertToInvoiceConfirmation());
        activities.add(new Authorize<RegularAcquisitionProcess>());
        activities.add(new UnAuthorize<RegularAcquisitionProcess>());

        activities.add(new AllocateProjectFundsPermanently<RegularAcquisitionProcess>());
        activities.add(new AllocateFundsPermanently<RegularAcquisitionProcess>());
        activities.add(new RemovePermanentProjectFunds<RegularAcquisitionProcess>());
        activities.add(new RemoveFundsPermanentlyAllocated<RegularAcquisitionProcess>());

        activities.add(new ProjectFundAllocation<RegularAcquisitionProcess>());
        activities.add(new FundAllocation<RegularAcquisitionProcess>());
        activities.add(new RemoveFundAllocation());
        activities.add(new RemoveProjectFundAllocation());
        activities.add(new RemoveFundAllocationExpirationDate());
        activities.add(new RemoveFundAllocationExpirationDateForResponsible());
        activities.add(new CancelAcquisitionRequest());

        activities.add(new UnlockInvoiceReceiving());
        activities.add(new LockInvoiceReceiving());

        activities.add(new SubmitForConfirmInvoice());
        activities.add(new ConfirmInvoice());
        activities.add(new CancelInvoiceConfirmation());

        activities.add(new UnSubmitForApproval());

        activities.add(new SetSkipSupplierFundAllocation());
        activities.add(new UnsetSkipSupplierFundAllocation());

        activities.add(new RevertInvoiceSubmission());
        activities.add(new RemoveCancelProcess());

        activities.add(new GenericAssignPayingUnitToItem<RegularAcquisitionProcess>());

        activities.add(new DistributeRealValuesForPayingUnits());

        activities.add(new ChangeFinancersAccountingUnit());
        activities.add(new SelectSupplier());

        activities.add(new ChangeProcessClassification());
        activities.add(new ChangeAcquisitionRequestItemClassification());
        activities.add(new CreateAcquisitionRequestItem());
        activities.add(new CreateAcquisitionRequestItemWithMaterial());
        activities.add(new PayAcquisition());
        activities.add(new DeleteAcquisitionRequestItem());
        activities.add(new EditAcquisitionRequestItem());
        activities.add(new EditAcquisitionRequestItemWithMaterial());
        activities.add(new EditAcquisitionRequestItemRealValues());
        activities.add(new TakeProcess<RegularAcquisitionProcess>());
        activities.add(new GiveProcess<RegularAcquisitionProcess>());
        activities.add(new ReleaseProcess<RegularAcquisitionProcess>());
        activities.add(new StealProcess<RegularAcquisitionProcess>());
        activities.add(new AddObserver<RegularAcquisitionProcess>());
        activities.add(new JumpToProcessState());
        activities.add(new EditSimpleContractDescription());

        activities.add(new ExceptionalChangeRequestingPerson());
        activities.add(new ChangeAcquisitionRequestItemMaterial());

        availableStates.add(AcquisitionProcessStateType.IN_GENESIS);
        availableStates.add(AcquisitionProcessStateType.SUBMITTED_FOR_APPROVAL);
        availableStates.add(AcquisitionProcessStateType.SUBMITTED_FOR_FUNDS_ALLOCATION);
        availableStates.add(AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER);
        availableStates.add(AcquisitionProcessStateType.FUNDS_ALLOCATED);
        availableStates.add(AcquisitionProcessStateType.AUTHORIZED);
        availableStates.add(AcquisitionProcessStateType.ACQUISITION_PROCESSED);
        availableStates.add(AcquisitionProcessStateType.INVOICE_RECEIVED);
        availableStates.add(AcquisitionProcessStateType.SUBMITTED_FOR_CONFIRM_INVOICE);
        availableStates.add(AcquisitionProcessStateType.INVOICE_CONFIRMED);
        availableStates.add(AcquisitionProcessStateType.FUNDS_ALLOCATED_PERMANENTLY);
        availableStates.add(AcquisitionProcessStateType.ACQUISITION_PAYED);
        availableStates.add(AcquisitionProcessStateType.REJECTED);
        availableStates.add(AcquisitionProcessStateType.CANCELED);
    }

    public static void registerActivity(
            WorkflowActivity<? extends RegularAcquisitionProcess, ? extends ActivityInformation<? extends RegularAcquisitionProcess>> activity) {
        activities.add(activity);
    }

    public static void registerActivityPredicate(final Class clazz, final BiPredicate predicate) {
        for (final WorkflowActivity activity : activities) {
            if (activity.getClass().equals(clazz)) {
                activity.registerIsActivePredicate(predicate);
            }
        }
    }

    protected SimplifiedProcedureProcess(final Person requester) {
        super();
        inGenesis();
        new AcquisitionRequest(this, requester);
    }

    protected SimplifiedProcedureProcess(Supplier supplier, Person person) {
        super();
        inGenesis();
        new AcquisitionRequest(this, supplier, person);
    }

    protected SimplifiedProcedureProcess(ProcessClassification classification, List<Supplier> suppliers, Person person) {
        super();
        inGenesis();
        AcquisitionRequest acquisitionRequest = new AcquisitionRequest(this, suppliers, person);
        if (suppliers.size() == 0) {
            throw new DomainException(Bundle.ACQUISITION, "acquisitionProcess.message.exception.needsMoreSuppliers");
        }
        if (suppliers.size() == 1) {
            acquisitionRequest.setSelectedSupplier(suppliers.get(0));
        }
        setProcessClassification(classification);
    }

    @Atomic
    public static SimplifiedProcedureProcess createNewAcquisitionProcess(
            final CreateAcquisitionProcessBean createAcquisitionProcessBean) {
        if (!isCreateNewProcessAvailable()) {
            throw new DomainException(Bundle.EXPENDITURE, "acquisitionProcess.message.exception.invalidStateToRun.create");
        }
        if (createAcquisitionProcessBean.isUnderMandatorySupplierScope()
                && !MissionSystem.getInstance().getMandatorySupplierSet().contains(createAcquisitionProcessBean.getSupplier())) {
            throw new DomainException(Bundle.ACQUISITION,
                    "acquisitionProcess.message.exception.manditory.supplier.for.this.scope", MissionSystem.getInstance()
                            .getMandatorySupplierNotUsedErrorMessageArg());
        }
        SimplifiedProcedureProcess process =
                new SimplifiedProcedureProcess(createAcquisitionProcessBean.getClassification(),
                        createAcquisitionProcessBean.getSuppliers(), createAcquisitionProcessBean.getRequester());
        AcquisitionRequest acquisitionRequest = process.getAcquisitionRequest();
        acquisitionRequest.setRequestingUnit(createAcquisitionProcessBean.getRequestingUnit());
        acquisitionRequest.setContractSimpleDescription(createAcquisitionProcessBean.getContractSimpleDescription());
        if (createAcquisitionProcessBean.isRequestUnitPayingUnit()) {
            final Unit unit = createAcquisitionProcessBean.getRequestingUnit();
            acquisitionRequest.addFinancers(unit.finance(acquisitionRequest));
        }
        if (createAcquisitionProcessBean.isForMission()) {
            if (createAcquisitionProcessBean.getMissionProcess() == null) {
                throw new DomainException(Bundle.ACQUISITION, "mission.process.is.mandatory");
            }
            process.setMissionProcess(createAcquisitionProcessBean.getMissionProcess());
        }
        if (createAcquisitionProcessBean.isUnderMandatorySupplierScope()
                && MissionSystem.getInstance().getMandatorySupplierSet().contains(createAcquisitionProcessBean.getSupplier())) {
            process.skipSupplierFundAllocation();
        }

        return process;
    }

    @Override
    public <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> List<T> getActivities() {
        return (List<T>) activities;
    }

    public <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> Stream<T> getActivityStream() {
        final List activities = this.activities;
        return activities.stream();
    }

    public boolean isEditRequestItemAvailable() {
        final Person loggedPerson = getLoggedPerson();
        return loggedPerson != null && loggedPerson.equals(getRequestor()) && getLastAcquisitionProcessState().isInGenesis();
    }

    @Override
    public Money getAcquisitionRequestValueLimit() {
        return getProcessClassification().getLimit();
    }

    @Override
    public boolean isSimplifiedAcquisitionProcess() {
        return true;
    }

    @Override
    public List<AcquisitionProcessStateType> getAvailableStates() {
        return availableStates;
    }

    public static List<AcquisitionProcessStateType> getAvailableStatesForSimplifiedProcedureProcess() {
        return availableStates;
    }

    @Override
    public boolean isSimplifiedProcedureProcess() {
        return true;
    }

    @Override
    public boolean isPayed() {
        return getRequest().isPayed();
    }

    @Override
    public String getProcessStateDescription() {
        return getLastAcquisitionProcessState().getLocalizedName();
    }

    @Override
    public boolean isAppiableForYear(final int year) {
        return Util.isAppiableForYear(year, this);
    }

    public boolean isCCP() {
        return getProcessClassification().isCCP();
    }

    @Override
    public void setProcessClassification(ProcessClassification processClassification) {
        if (getSkipSupplierFundAllocation()) {
            unSkipSupplierFundAllocation();
        }
        if (processClassification.getLimit().isLessThan(this.getAcquisitionRequest().getCurrentValue())) {
            throw new DomainException(Bundle.ACQUISITION, "error.message.processValueExceedsLimitForClassification");
        }
        super.setProcessClassification(processClassification);
    }

    public void setProcessClassificationWithoutChecks(ProcessClassification processClassification) {
        super.setProcessClassification(processClassification);
    }

    public boolean isWarnRegardingProcessClassificationNeeded() {
        return getProcessClassification().isCCP() != getRequestingUnit().getDefaultRegeimIsCCP();
    }

    @Override
    public Boolean getShouldSkipSupplierFundAllocation() {
        return !this.isCCP() || super.getShouldSkipSupplierFundAllocation();
    }

    public boolean isWarnForLessSuppliersActive() {
        return getProcessClassification() == ProcessClassification.CT75000 && getSuppliers().size() < 3;
    }

    @Override
    public List<Class<? extends ProcessFile>> getAvailableFileTypes() {
        List<Class<? extends ProcessFile>> availableFileTypes = new ArrayList<Class<? extends ProcessFile>>();
        availableFileTypes.add(AcquisitionProposalDocument.class);
        availableFileTypes.add(PurchaseOrderDocument.class);
        availableFileTypes.add(AcquisitionInvoice.class);
        availableFileTypes.add(CreditNoteDocument.class);
        availableFileTypes.addAll(super.getAvailableFileTypes());
        return availableFileTypes;
    }

    @Override
    public List<Class<? extends ProcessFile>> getUploadableFileTypes() {
        List<Class<? extends ProcessFile>> uploadableFileTypes = super.getUploadableFileTypes();
        uploadableFileTypes.remove(PurchaseOrderDocument.class);
        return uploadableFileTypes;
    }

    @Override
    public String getLocalizedName() {
        return getProcessClassification().getLocalizedName();
    }

    @Override
    public String getTypeDescription() {
        return getProcessClassification().getLocalizedName();
    }

    @Override
    public String getTypeShortDescription() {
        return getProcessClassification().getShortDescription();
    }

    @Override
    public void processAcquisition() {
        super.processAcquisition();
        ProcessClassification processClassification = getProcessClassification();
        if ((processClassification == ProcessClassification.CT75000 || processClassification == ProcessClassification.CT10000)
                && !getAcquisitionRequest().hasAnnouncement()) {
            new RCISTAnnouncement(getAcquisitionRequest());
        }
    }

    @Override
    public PresentableProcessState getPresentableAcquisitionProcessState() {
        return getLastAcquisitionProcessState().getAcquisitionProcessStateType();
    }

    @Override
    public List<? extends PresentableProcessState> getAvailablePresentableStates() {
        return getAvailableStates();
    }

    public boolean getFundAllocationPresent() {
        for (final Financer financer : getAcquisitionRequest().getFinancers()) {
            if (financer.isFundAllocationPresent()) {
                return true;
            }
        }
        return false;
    }

    public boolean getEffectiveFundAllocationPresent() {
        for (final Financer financer : getAcquisitionRequest().getFinancers()) {
            if (financer.isEffectiveFundAllocationPresent()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSomeInvoice() {
        return isInvoiceReceived()
                || (getExpenditureTrackingSystem().isInvoiceAllowedToStartAcquisitionProcess() && hasInvoiceFile());
    }

    @Override
    public boolean hasInvoiceFile() {
        for (final ProcessFile processFile : getFilesSet()) {
            if (processFile instanceof AcquisitionInvoice && !processFile.isArchieved()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean isProcessessStartedWithInvoive() {
        return hasInvoiceFile() && !hasAcquisitionProposalDocument();
    }

    @Override
    public AcquisitionItemClassification getGoodsOrServiceClassification() {
        final AcquisitionRequest request = getRequest();
        return request.getGoodsOrServiceClassification();
    }

    @Deprecated
    public boolean hasProcessClassification() {
        return getProcessClassification() != null;
    }

    @Override
    public void confirmInvoiceBy(final Person person) {
        super.confirmInvoiceBy(person);
        getFileStream(AcquisitionInvoice.class)
            .map(f -> (AcquisitionInvoice) f)
            .filter(i -> i.getState() == AcquisitionInvoiceState.AWAITING_CONFIRMATION)
            .filter(i -> i.isConfirmedByForAllUnits())
            .forEach(i -> i.setState(AcquisitionInvoiceState.CONFIRMED));
    }

    public boolean areAllInvoicesRegistered() {
        return getAcquisitionRequest().getAcquisitionRequestItemStream().allMatch(i -> i.areAllInvoicesRegistered());
    }

    public boolean areAllInvoicesRegisteredAndPayed() {
        return getAcquisitionRequest().getAcquisitionRequestItemStream().allMatch(i -> i.areAllInvoicesRegisteredAndPayed());
    }

    public boolean hasInvoicePendingPayment() {
        return getFileStream(AcquisitionInvoice.class)
            .map(i -> (AcquisitionInvoice) i)
            .anyMatch(i -> i.getState() == AcquisitionInvoiceState.PROCESSED);
    }

}
