package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class ProcessCanceledPersonnelActivity extends ProcessPersonnelActivity {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
	return (!missionProcess.hasCurrentOwner() || missionProcess.isTakenByCurrentUser())
		&& missionProcess.getIsCanceled().booleanValue() && missionProcess.hasAnyCurrentQueues()
		&& missionProcess.isCurrentUserAbleToAccessAnyQueues() && missionProcess.isAuthorized()
		&& missionProcess.areAllParticipantsAuthorized();
    }

}
