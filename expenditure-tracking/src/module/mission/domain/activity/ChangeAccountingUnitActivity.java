package module.mission.domain.activity;

import module.mission.domain.Mission;
import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import myorg.domain.RoleType;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;

public class ChangeAccountingUnitActivity extends MissionProcessActivity<MissionProcess, ChangeAccountingUnitActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
	final Mission mission = missionProcess.getMission();
	return super.isActive(missionProcess, user)
		&& (missionProcess.isUnderConstruction() && missionProcess.isRequestor(user)
			|| user.hasRoleType(RoleType.MANAGER))
		&& mission.getFinancerCount() > 0;
    }

    @Override
    protected void process(final ChangeAccountingUnitActivityInformation activityInformation) {
	final MissionFinancer financer = activityInformation.getFinancer();
	final AccountingUnit accountingUnit = activityInformation.getAccountingUnit();
	financer.setAccountingUnit(accountingUnit);
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
	return new ChangeAccountingUnitActivityInformation(process, this);
    }

    @Override
    public boolean isVisible() {
	return false;
    }

}
