package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalRequest;
import module.workingCapital.domain.WorkingCapitalSystem;
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
	final WorkingCapitalInitialization workingCapitalInitialization = workingCapital.getWorkingCapitalInitialization();
	final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstance();
	return workingCapitalInitialization != null
		&& !workingCapital.isCanceledOrRejected()
		&& workingCapitalSystem.isAccountingMember(user)
		&& workingCapitalInitialization.isAuthorized()
		&& !workingCapital.hasAnyPendingWorkingCapitalRequests()
		&& !workingCapital.hasAcquisitionPendingVerification();
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

}
