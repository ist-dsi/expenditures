package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import myorg.domain.User;
import myorg.domain.exceptions.DomainException;
import myorg.util.BundleUtil;

public class ProcessPersonnelActivity extends MissionProcessActivity<MissionProcess, ActivityInformation<MissionProcess>> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
	return super.isActive(missionProcess, user)
		//&& !missionProcess.getIsCanceled().booleanValue()
		&& missionProcess.hasAnyCurrentQueues() && missionProcess.isCurrentUserAbleToAccessAnyQueues()
		&& (missionProcess.isAuthorized() || missionProcess.hasNoItemsAndParticipantesAreAuthorized())
		&& missionProcess.areAllParticipantsAuthorized();
    }

    @Override
    protected void process(final ActivityInformation activityInformation) {
	final MissionProcess missionProcess = (MissionProcess) activityInformation.getProcess();
	if (missionProcess.getCurrentQueuesCount() > 1) {
	    throw new DomainException(
		    "Cannot determine which queue to remove because the mission process is associated to several queues.");
	}
	missionProcess.removeCurrentQueues(missionProcess.getCurrentQueues().get(0));
    }

}
