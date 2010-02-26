package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class RejectWorkingCapitalInitializationActivity extends WorkflowActivity<WorkingCapitalProcess, WorkingCapitalInitializationInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	return !workingCapital.isCanceledOrRejected()
		&& (workingCapital.isPendingAproval(user)
			|| workingCapital.isPendingVerification(user)
			|| workingCapital.isPendingAuthorization(user));
    }

    @Override
    protected void process(final WorkingCapitalInitializationInformation activityInformation) {
	final WorkingCapitalInitialization workingCapitalInitialization = activityInformation.getWorkingCapitalInitialization();
	workingCapitalInitialization.reject();
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
	final WorkingCapitalInitializationInformation workingCapitalInitializationInformation = new WorkingCapitalInitializationInformation(process, this);
	final WorkingCapital workingCapital = process.getWorkingCapital();
	final WorkingCapitalInitialization workingCapitalInitialization = workingCapital.getSortedWorkingCapitalInitializations().last();
	workingCapitalInitializationInformation.setWorkingCapitalInitialization(workingCapitalInitialization);
	return workingCapitalInitializationInformation;
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
	return true;
    }

    @Override
    public boolean isConfirmationNeeded(final WorkingCapitalProcess process) {
        return true;
    }

    @Override
    public String getUsedBundle() {
	return "resources/WorkingCapitalResources";
    }

}
