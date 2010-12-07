package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class ApproveActivity extends MissionProcessActivity<MissionProcess, LateJustificationActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
	return super.isActive(missionProcess, user) && !missionProcess.isUnderConstruction() && !missionProcess.getIsCanceled() && missionProcess.isPendingApprovalBy(user);
    }

    @Override
    protected void process(final LateJustificationActivityInformation activityInformation) {
	final MissionProcess missionProcess = (MissionProcess) activityInformation.getProcess();
	final User user = UserView.getCurrentUser();
	missionProcess.approve(user);
	if (!missionProcess.isOnTime()) {
	    missionProcess.justifyLateSubmission(activityInformation.getJustification());
	}
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(MissionProcess process) {
        return new LateJustificationActivityInformation(process, this);
    }

    @Override
    public boolean isConfirmationNeeded(final MissionProcess process) {
        return true;
    }

    @Override
    public String getLocalizedConfirmationMessage() {
	return BundleUtil.getFormattedStringFromResourceBundle("resources/MissionResources",
		"label.module.mission.approve.confirm.service.is.assured");
    }

}
