package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import myorg.domain.RoleType;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class SubmitForApprovalByManagerOrManagementCouncilActivity extends MissionProcessActivity<MissionProcess, ActivityInformation<MissionProcess>> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + SubmitForApprovalActivity.class.getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
	return super.isActive(missionProcess, user)
		&& missionProcess.isUnderConstruction()
		&& user.hasRoleType(RoleType.MANAGER)
		;
    }

    @Override
    protected void process(final ActivityInformation<MissionProcess> activityInformation) {
	final MissionProcess missionProcess = (MissionProcess) activityInformation.getProcess();
	//missionProcess.checkForAnyOverlappingParticipations();
	missionProcess.checkForSupportDocuments();
	missionProcess.setIsUnderConstruction(Boolean.FALSE);
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(MissionProcess process) {
        return new ActivityInformation<MissionProcess>(process, this);
    }

}
