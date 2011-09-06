package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.ExceptionalWorkingCapitalRefund;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.RoleType;
import myorg.domain.User;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;

public class ExceptionalCapitalRestitutionActivity extends
	WorkflowActivity<WorkingCapitalProcess, ExceptionalCapitalRestitutionInfo> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity."
		+ getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	return user.hasRoleType(RoleType.MANAGER);
    }

    @Override
    protected void process(final ExceptionalCapitalRestitutionInfo activityInformation) {
	final WorkingCapital workingCapital = activityInformation.getProcess().getWorkingCapital();
	final Money value = activityInformation.getValue();
	final String description = activityInformation.getCaseDescription();
	new ExceptionalWorkingCapitalRefund(workingCapital, getLoggedPerson().getPerson(), value, description);
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
	return new ExceptionalCapitalRestitutionInfo(process, this);
    }

    @Override
    public String getUsedBundle() {
	return "resources/WorkingCapitalResources";
    }

    @Override
    public boolean isUserAwarenessNeeded(WorkingCapitalProcess process) {
	return false;
    }

    @Override
    public boolean isConfirmationNeeded(WorkingCapitalProcess process) {
	return true;
    }
}
