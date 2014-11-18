package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;

public class UnApproveActivity extends MissionProcessActivity<MissionProcess, ActivityInformation<MissionProcess>> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/MissionResources", "activity." + getClass().getSimpleName());
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
        final User user = Authenticate.getUser();
        missionProcess.unapprove(user);
        missionProcess.getMission().setIsVerified(false);
        missionProcess.removeFromVerificationQueue();
    }

}
