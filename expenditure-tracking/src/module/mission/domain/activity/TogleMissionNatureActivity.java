package module.mission.domain.activity;

import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import myorg.domain.RoleType;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class TogleMissionNatureActivity extends MissionProcessActivity<MissionProcess, ActivityInformation<MissionProcess>> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
	return super.isActive(missionProcess, user)
		&& user.hasRoleType(RoleType.MANAGER)
		&& missionProcess.canTogleMissionNature();
    }

    @Override
    protected void process(final ActivityInformation<MissionProcess> activityInformation) {
	final MissionProcess missionProcess = activityInformation.getProcess();
	final Mission mission = missionProcess.getMission();
	final boolean booleanValue = mission.getGrantOwnerEquivalence().booleanValue();
	mission.setGrantOwnerEquivalence(Boolean.valueOf(!booleanValue));
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
	return new ActivityInformation<MissionProcess>(process, this);
    }

}
