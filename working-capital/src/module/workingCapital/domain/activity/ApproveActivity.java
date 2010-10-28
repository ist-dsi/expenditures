package module.workingCapital.domain.activity;

import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class ApproveActivity extends WorkflowActivity<WorkingCapitalProcess, WorkingCapitalInitializationInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess process, final User user) {
	final WorkingCapital workingCapital = process.getWorkingCapital();
	return !workingCapital.isCanceledOrRejected() && workingCapital.isPendingAproval(user);
    }

    @Override
    protected void process(final WorkingCapitalInitializationInformation activityInformation) {
	final WorkingCapitalInitialization workingCapitalInitialization = activityInformation.getWorkingCapitalInitialization();
	final User user = getLoggedPerson();
	final Person person = user.getPerson();
	workingCapitalInitialization.approve(person);
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new WorkingCapitalInitializationInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
	return true;
    }

    @Override
    public boolean isVisible() {
	return false;
    }

    @Override
    public boolean isUserAwarenessNeeded(WorkingCapitalProcess process, User user) {
	final WorkingCapital workingCapital = process.getWorkingCapital();
        return !workingCapital.isCanceledOrRejected() && workingCapital.isPendingDirectAproval(user);
    }
}
