package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class AuthorizeActivity extends MissionProcessActivity<MissionProcess, ActivityInformation<MissionProcess>> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
	return super.isActive(missionProcess, user)
		&& missionProcess.isApproved()
		&& missionProcess.hasAllAllocatedFunds()
		&& missionProcess.areAllParticipantsAuthorized()
		&& missionProcess.isPendingAuthorizationBy(user);
    }

    @Override
    protected void process(final ActivityInformation activityInformation) {
	final User user = UserView.getCurrentUser();
	final MissionProcess missionProcess = (MissionProcess) activityInformation.getProcess();
	missionProcess.authorize(user);
    }

}
