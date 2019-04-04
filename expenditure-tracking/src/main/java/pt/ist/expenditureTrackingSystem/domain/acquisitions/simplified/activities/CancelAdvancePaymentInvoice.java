package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionInvoiceState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class CancelAdvancePaymentInvoice
        extends WorkflowActivity<SimplifiedProcedureProcess, CancelAdvancePaymentInvoiceInformation> {

    @Override
    public boolean isActive(SimplifiedProcedureProcess process, User user) {
        return isUserProcessOwner(process, user) && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user)
                && hasConfirmedAdvancePaymentInvoice(process);
    }

    private boolean hasConfirmedAdvancePaymentInvoice(final SimplifiedProcedureProcess process) {
        return process.getConfirmedInvoices(null).stream()
                .anyMatch(i -> i.getAdvancePaymentInvoice() && i.getState() == AcquisitionInvoiceState.PAYED);
    }

    @Override
    protected void process(CancelAdvancePaymentInvoiceInformation activityInformation) {
        final SimplifiedProcedureProcess process = activityInformation.getProcess();
        process.unconfirmInvoiceForAll(activityInformation.getInvoice());
        //process.cancelFundAllocationRequest(true);
        process.setStateToMinimumAcquisitionInvoiceState();
    }

    @Override
    public ActivityInformation<SimplifiedProcedureProcess> getActivityInformation(final SimplifiedProcedureProcess process) {
        return new CancelAdvancePaymentInvoiceInformation(process, this);
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
        return "resources/AcquisitionResources";
    }

    @Override
    public boolean isUserAwarenessNeeded(SimplifiedProcedureProcess process, User user) {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
