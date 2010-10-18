package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class ChangeAccountingUnitActivity extends WorkflowActivity<WorkingCapitalProcess, ChangeAccountingUnitActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	return !workingCapital.isCanceledOrRejected()
		&& workingCapital.canChangeAccountingUnit()
		&& ((workingCapital.isRequester(user) && (workingCapital.isPendingAproval() || workingCapital.isPendingAcceptResponsability()))
			|| workingCapital.isPendingVerification(user));
    }

    @Override
    protected void process(final ChangeAccountingUnitActivityInformation activityInformation) {
	final WorkingCapitalProcess process = activityInformation.getProcess();
	final WorkingCapital workingCapital = process.getWorkingCapital();
	final WorkingCapitalInitialization workingCapitalInitialization = workingCapital.getWorkingCapitalInitialization();
	workingCapitalInitialization.setAccountingUnit(activityInformation.getAccountingUnit());
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new ChangeAccountingUnitActivityInformation(process, this);
    }

}
