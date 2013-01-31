package module.mission.domain.activity;

import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;

public class RevertMissionForEditingActivity extends
		MissionProcessActivity<MissionProcess, RevertMissionForEditingActivityInformation> {

	@Override
	public String getLocalizedName() {
		return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
	}

	@Override
	public boolean isActive(final MissionProcess missionProcess, final User user) {
		return super.isActive(missionProcess, user) && !missionProcess.isUnderConstruction() && !missionProcess.isCanceled()
				&& missionProcess.isRequestor(user)
				&& missionProcess.getMission().getMissionVersion().getChangesAfterArrival() == null;
	}

	@Override
	protected void process(final RevertMissionForEditingActivityInformation activityInformation) {
		final MissionProcess missionProcess = activityInformation.getProcess();
		final Mission mission = missionProcess.getMission();
		mission.revertMissionForEditing(activityInformation.getDescription());
		missionProcess.setIsUnderConstruction(Boolean.TRUE);
	}

	@Override
	public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
		return new RevertMissionForEditingActivityInformation(process, this);
	}

	@Override
	public boolean isDefaultInputInterfaceUsed() {
		return false;
	}

}
