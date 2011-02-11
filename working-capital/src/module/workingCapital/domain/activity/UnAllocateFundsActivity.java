package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class UnAllocateFundsActivity extends WorkflowActivity<WorkingCapitalProcess, WorkingCapitalInitializationInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity."
		+ getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess workingCapitalProcess, final User user) {
	final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();
	return workingCapital.isPendingFundUnAllocation(user);
    }

    @Override
    protected void process(final WorkingCapitalInitializationInformation activityInformation) {
	WorkingCapitalProcess process = activityInformation.getProcess();
	final WorkingCapitalInitialization workingCapitalInitialization = process.getWorkingCapital()
		.getWorkingCapitalInitialization();
	workingCapitalInitialization.setFundAllocationId(null);
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
	return new WorkingCapitalInitializationInformation(process, this) {
	    @Override
	    public boolean hasAllneededInfo() {
		return true;
	    }
	};
    }

    @Override
    public boolean isConfirmationNeeded(WorkingCapitalProcess process) {
	return true;
    }

    @Override
    public String getUsedBundle() {
	return "resources/WorkingCapitalResources";
    }

    @Override
    public boolean isVisible() {
	return true;
    }

}
