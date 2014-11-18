package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.mission.domain.util.MissionState;
import module.workflow.activities.ActivityInformation;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class UnCommitFundsActivity extends MissionProcessActivity<MissionProcess, UnCommitFundsActivityInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        if (!super.isActive(missionProcess, user)) {
            return false;
        }
        if (missionProcess.isCanceled()) {
            return false;
        }
        if (!MissionState.FUND_ALLOCATION.isPending(missionProcess)
                && !MissionState.PARTICIPATION_AUTHORIZATION.isPending(missionProcess)) {
            return false;
        }
        if (!missionProcess.hasCommitmentNumber()) {
            return false;
        }
        if (missionProcess.hasAnyAuthorizedParticipants()) {
            return false;
        }
        return missionProcess.isAccountingEmployee(user.getExpenditurePerson());
    }

    @Override
    protected void process(final UnCommitFundsActivityInformation unCommitFundsActivityInformation) {
        unCommitFundsActivityInformation.getMissionFinancer().setCommitmentNumber(null);
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
        return new UnCommitFundsActivityInformation(process, this);
    }

    @Override
    public boolean isVisible() {
        return false;
    }

}
