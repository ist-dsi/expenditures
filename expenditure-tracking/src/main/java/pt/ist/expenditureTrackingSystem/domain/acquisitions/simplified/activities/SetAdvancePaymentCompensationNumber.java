package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import com.google.common.base.Strings;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class SetAdvancePaymentCompensationNumber
        extends WorkflowActivity<SimplifiedProcedureProcess, SetAdvancePaymentCompensationNumberInformation> {

    @Override
    public boolean isActive(SimplifiedProcedureProcess process, User user) {
        Person person = user.getExpenditurePerson();
        return isUserProcessOwner(process, user) && process.isAccountingEmployee(person)
                && process.getSignedAdvancePaymentDocument() != null
                && !process.getAcquisitionRequest().getPaymentSet().isEmpty();
    }

    @Override
    protected void process(SetAdvancePaymentCompensationNumberInformation activityInformation) {
        SimplifiedProcedureProcess process = activityInformation.getProcess();
        activityInformation.getPayment().setCompensationNumber(activityInformation.getCompensationNumber());
        if (process.areAllInvoicesRegisteredAndPayed() && allAdvancePaymentInvoicesCompensated(process)) {
            process.acquisitionPayed();
        }
    }

    private boolean allAdvancePaymentInvoicesCompensated(SimplifiedProcedureProcess process) {
        return process.getAcquisitionRequest().getPaymentSet().stream()
                .allMatch(p -> !Strings.isNullOrEmpty(p.getCompensationNumber()));
    }

    @Override
    public ActivityInformation<SimplifiedProcedureProcess> getActivityInformation(SimplifiedProcedureProcess process) {
        return new SetAdvancePaymentCompensationNumberInformation(process, this);
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
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean isUserAwarenessNeeded(SimplifiedProcedureProcess process) {
        return process.getAcquisitionProcessState().getAcquisitionProcessStateType()
                .ordinal() == AcquisitionProcessStateType.FUNDS_ALLOCATED_PERMANENTLY.ordinal();
    }
}
