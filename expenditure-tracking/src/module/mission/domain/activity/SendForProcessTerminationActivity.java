package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class SendForProcessTerminationActivity extends MissionProcessActivity<MissionProcess, ActivityInformation<MissionProcess>> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
	return super.isActive(missionProcess, user) && missionProcess.isReadyForMissionTermination(user);
    }

    @Override
    protected void process(final ActivityInformation<MissionProcess> activityInformation) {
	final MissionProcess missionProcess = (MissionProcess) activityInformation.getProcess();
	missionProcess.sendForProcessTermination(null);
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(MissionProcess process) {
        return new ActivityInformation<MissionProcess>(process, this);
    }

    @Override
    public boolean isConfirmationNeeded(MissionProcess process) {
	return true;
    }

    @Override
    public String getLocalizedConfirmationMessage() {
	return BundleUtil.getFormattedStringFromResourceBundle("resources/MissionResources",
		"label.module.mission.SendForProcessTerminationActivity.confirmation");
    }

}
