package module.mission.domain.activity;

import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionProcess;
import module.mission.domain.activity.CommitFundsActivityInformation.MissionFinancerCommitFundAllocationBean;
import module.mission.domain.util.MissionState;
import module.workflow.activities.ActivityInformation;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class CommitFundsActivity extends MissionProcessActivity<MissionProcess, CommitFundsActivityInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return super.isActive(missionProcess, user) && MissionState.FUND_ALLOCATION.isPending(missionProcess)
                && !missionProcess.isCanceled() && missionProcess.hasAllAllocatedFunds()
                && !missionProcess.hasAllCommitmentNumbers() && missionProcess.isAccountingEmployee(user.getExpenditurePerson());
    }

    @Override
    protected void process(final CommitFundsActivityInformation commitFundsActivityInformation) {
        for (final MissionFinancerCommitFundAllocationBean missionFinancerCommitFundAllocationBean : commitFundsActivityInformation
                .getMissionFinancerCommitFundAllocationBeans()) {
            final MissionFinancer missionFinancer = missionFinancerCommitFundAllocationBean.getMissionFinancer();
            final String commitmentNumber = missionFinancerCommitFundAllocationBean.getCommitmentNumber();
            missionFinancer.setCommitmentNumber(commitmentNumber);
        }
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
        return new CommitFundsActivityInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

}
