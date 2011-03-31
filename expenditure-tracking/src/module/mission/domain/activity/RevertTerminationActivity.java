package module.mission.domain.activity;

import module.mission.domain.MissionItem;
import module.mission.domain.MissionProcess;
import module.mission.domain.MissionVersion;
import module.workflow.activities.ActivityInformation;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class RevertTerminationActivity extends MissionProcessActivity<MissionProcess, ActivityInformation<MissionProcess>> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
	return super.isActive(missionProcess, user)
		&& missionProcess.isTerminated()
		&& (!missionProcess.isArchived()
			|| (missionProcess.isArchived() && !missionProcess.getMission().hasAnyFinancer()))
		&& missionProcess.canArchiveMission();
    }

    @Override
    protected void process(final ActivityInformation<MissionProcess> activityInformation) {
	final MissionProcess missionProcess = activityInformation.getProcess();
	missionProcess.revertProcessTermination();
    }

    private boolean areAllMissionItemFinancersArchived(final MissionVersion missionVersion) {
	for (final MissionItem missionItem : missionVersion.getMissionItemsSet()) {
	    if (!missionItem.isArchived()) {
		return false;
	    }
	}
	return true;
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
	return new ActivityInformation(process, this);
    }

    @Override
    public boolean isConfirmationNeeded(MissionProcess process) {
	return true;
    }

    @Override
    public String getLocalizedConfirmationMessage() {
	return BundleUtil.getFormattedStringFromResourceBundle("resources/MissionResources",
		"label.module.mission.revert.termination");
    }

}
