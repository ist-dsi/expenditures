package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;

public class UnApproveActivity extends MissionProcessActivity<MissionProcess, ActivityInformation<MissionProcess>> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return super.isActive(missionProcess, user) && missionProcess.canRemoveApproval(user)
                && !missionProcess.hasAnyAllocatedFunds() && !missionProcess.hasAnyAllocatedProjectFunds()
                && !missionProcess.hasAnyAuthorizedParticipants() && !missionProcess.hasAnyAuthorization();
    }

    @Override
    protected void process(final ActivityInformation activityInformation) {
        final MissionProcess missionProcess = (MissionProcess) activityInformation.getProcess();
        final User user = UserView.getCurrentUser();
        missionProcess.unapprove(user);
        missionProcess.getMission().setIsVerified(false);
        missionProcess.removeFromVerificationQueue();
    }

}
