package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class UnProcessPersonnelActivity extends MissionProcessActivity<MissionProcess, ActivityInformation<MissionProcess>> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
	return super.isActive(missionProcess, user)
		&& !missionProcess.hasCurrentQueue()
		&& missionProcess.areAllParticipantsAuthorized()
		&& missionProcess.isCurrentUserAbleToAccessQueue();
    }

    @Override
    protected void process(final ActivityInformation<MissionProcess> activityInformation) {
	final MissionProcess missionProcess = activityInformation.getProcess();
	missionProcess.setProcessParticipantInformationQueue();
    }

}
