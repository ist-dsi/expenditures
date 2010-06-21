package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalInitializationReenforcement;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class VerifyActivity extends WorkflowActivity<WorkingCapitalProcess, VerifyActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	return !workingCapital.isCanceledOrRejected() && workingCapital.isPendingVerification(user);
    }

    @Override
    protected void process(final VerifyActivityInformation activityInformation) {
	final User user = getLoggedPerson();
	final WorkingCapitalInitialization workingCapitalInitialization = activityInformation.getWorkingCapitalInitialization();
	workingCapitalInitialization.verify(user, activityInformation.getAuthorizedAnualValue(), activityInformation.getMaxAuthorizedAnualValue(), activityInformation.getFundAllocationId());
	if (workingCapitalInitialization instanceof WorkingCapitalInitializationReenforcement) {
	    final WorkingCapitalInitializationReenforcement workingCapitalInitializationReenforcement = (WorkingCapitalInitializationReenforcement) workingCapitalInitialization;
	    workingCapitalInitializationReenforcement.setAuthorizedReenforcementValue(activityInformation.getAuthorizedReenforcementValue());
	}
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new VerifyActivityInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
	return false;
    }

    @Override
    public boolean isVisible() {
	return false;
    }

}
