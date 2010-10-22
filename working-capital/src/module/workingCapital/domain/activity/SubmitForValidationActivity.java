package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalAcquisitionSubmission;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.User;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;

import org.joda.time.DateTime;

public class SubmitForValidationActivity extends WorkflowActivity<WorkingCapitalProcess, SubmitForValidationActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	return !workingCapital.isCanceledOrRejected()
		&& workingCapital.isMovementResponsible(user)
		&& workingCapital.hasApprovedAndUnSubmittedAcquisitions()
		&& !workingCapital.hasAcquisitionPendingApproval();
    }

    @Override
    protected void process(final SubmitForValidationActivityInformation activityInformation) {
	final WorkingCapitalProcess workingCapitalProcess = activityInformation.getProcess();
	workingCapitalProcess.submitAcquisitionsForValidation();
	final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();
	if (activityInformation.isLastSubmission()) {
	    final WorkingCapitalInitialization workingCapitalInitialization = workingCapital.getWorkingCapitalInitialization();
	    workingCapitalInitialization.setLastSubmission(new DateTime());
	}
	final Money accumulatedValue = workingCapital.getLastTransaction().getAccumulatedValue();
	new WorkingCapitalAcquisitionSubmission(workingCapital, getLoggedPerson().getPerson(), accumulatedValue);
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new SubmitForValidationActivityInformation(process, this);
    }

    @Override
    public boolean isUserAwarenessNeeded(final WorkingCapitalProcess process, final User user) {
        return false;
    }

}
