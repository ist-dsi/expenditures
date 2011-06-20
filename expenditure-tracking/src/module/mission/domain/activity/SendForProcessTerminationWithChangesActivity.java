package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class SendForProcessTerminationWithChangesActivity extends
	MissionProcessActivity<MissionProcess, SendForProcessTerminationWithChangesActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
	return super.isActive(missionProcess, user) && missionProcess.isReadyForMissionTermination(user);
    }

    @Override
    protected void process(final SendForProcessTerminationWithChangesActivityInformation activityInformation) {
	final MissionProcess missionProcess = (MissionProcess) activityInformation.getProcess();
	missionProcess.sendForProcessTermination(activityInformation.getDescriptionOfChangesAfterArrival());
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(MissionProcess process) {
	return new SendForProcessTerminationWithChangesActivityInformation(process, this);
    }

    @Override
    public boolean isConfirmationNeeded(MissionProcess process) {
	return true;
    }

    @Override
    public String getLocalizedConfirmationMessage() {
	return BundleUtil.getFormattedStringFromResourceBundle("resources/MissionResources",
		"label.module.mission.SendForProcessTerminationWithChangesActivity.confirmation")
		+ "<br/>"
		+ "<br/>"
		+ BundleUtil.getFormattedStringFromResourceBundle("resources/MissionResources",
			"label.module.mission.SendForProcessTerminationWithChangesActivity.confirmation.next.page");
    }

}
