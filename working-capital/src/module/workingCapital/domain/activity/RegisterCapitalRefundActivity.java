package module.workingCapital.domain.activity;

import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalRefund;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class RegisterCapitalRefundActivity extends WorkflowActivity<WorkingCapitalProcess, RegisterCapitalRefundActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	final WorkingCapitalInitialization workingCapitalInitialization = workingCapital.getWorkingCapitalInitialization();
	return workingCapitalInitialization != null
		&& workingCapitalInitialization.getRefundRequested() != null
		&& workingCapital.isTreasuryMember(user)
		&& workingCapital.getBalance().isPositive();
    }

    @Override
    protected void process(final RegisterCapitalRefundActivityInformation activityInformation) {
	if (activityInformation.isConfirmed()) {
	    final WorkingCapitalProcess process = activityInformation.getProcess();
	    final WorkingCapital workingCapital = process.getWorkingCapital();
	    final Person person = getLoggedPerson().getPerson();
	    new WorkingCapitalRefund(workingCapital, person, workingCapital.getBalance());
	}
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new RegisterCapitalRefundActivityInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

}
