package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalRequest;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class RequestCapitalActivity extends WorkflowActivity<WorkingCapitalProcess, RequestCapitalActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	return (workingCapital.isAccountingResponsible(user) || workingCapital.isAccountingEmployee(user))
		&& workingCapital.canRequestCapital();
    }

    @Override
    protected void process(final RequestCapitalActivityInformation activityInformation) {
	final WorkingCapitalProcess workingCapitalProcess = activityInformation.getProcess();
	final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();
	new WorkingCapitalRequest(workingCapital, activityInformation.getRequestedValue(), activityInformation.getPaymentMethod());
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new RequestCapitalActivityInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

    @Override
    public boolean isUserAwarenessNeeded(final WorkingCapitalProcess process, final User user) {
        return false;
    }

}
