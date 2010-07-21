package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalInitializationReenforcement;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class ReenforceWorkingCapitalInitializationActivity extends
	WorkflowActivity<WorkingCapitalProcess, ReenforceWorkingCapitalInitializationActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity."
		+ getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	final WorkingCapitalInitialization workingCapitalInitialization = workingCapital.getWorkingCapitalInitialization();
	return workingCapital.hasMovementResponsible()
		&& workingCapital.getMovementResponsible().getUser() == user
		&& !workingCapital.isCanceledOrRejected()
		&& workingCapitalInitialization != null
		&& workingCapitalInitialization.getLastSubmission() == null
		&& (workingCapitalInitialization.isAuthorized() || workingCapitalInitialization.isCanceledOrRejected());
    }

    @Override
    protected void process(final ReenforceWorkingCapitalInitializationActivityInformation activityInformation) {
	final WorkingCapitalProcess workingCapitalProcess = activityInformation.getProcess();
	final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();
	final WorkingCapitalInitialization workingCapitalInitialization = workingCapital.getWorkingCapitalInitialization();
	new WorkingCapitalInitializationReenforcement(workingCapitalInitialization, activityInformation.getAmount());
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
	return new ReenforceWorkingCapitalInitializationActivityInformation(process, this);
    }

}
