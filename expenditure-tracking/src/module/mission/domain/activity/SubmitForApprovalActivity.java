package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class SubmitForApprovalActivity extends MissionProcessActivity<MissionProcess, LateJustificationActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
	return super.isActive(missionProcess, user)
		&& missionProcess.isUnderConstruction()
		&& missionProcess.isRequestor(user)
		&& missionProcess.isConsistent();
    }

    @Override
    protected void process(final LateJustificationActivityInformation activityInformation) {
	final MissionProcess missionProcess = (MissionProcess) activityInformation.getProcess();
	missionProcess.checkForAnyOverlappingParticipations();
	missionProcess.setIsUnderConstruction(Boolean.FALSE);
	if (!missionProcess.isOnTime()) {
	    missionProcess.justifyLateSubmission(activityInformation.getJustification());
	}
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(MissionProcess process) {
        return new LateJustificationActivityInformation(process, this);
    }

}
