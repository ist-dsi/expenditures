package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalRequest;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class UnRequestCapitalActivity extends WorkflowActivity<WorkingCapitalProcess, UnRequestCapitalActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	return (workingCapital.isAccountingResponsible(user) || workingCapital.isAccountingEmployee(user))
		&& workingCapital.isPendingPayment();
    }

    @Override
    protected void process(final UnRequestCapitalActivityInformation activityInformation) {
	final WorkingCapitalRequest workingCapitalRequest = activityInformation.getWorkingCapitalRequest();
	workingCapitalRequest.delete();
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new UnRequestCapitalActivityInformation(process, this);
    }

    @Override
    public boolean isUserAwarenessNeeded(final WorkingCapitalProcess process, final User user) {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

}
