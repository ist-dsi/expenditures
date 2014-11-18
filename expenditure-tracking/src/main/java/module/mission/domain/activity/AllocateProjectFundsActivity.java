package module.mission.domain.activity;

import module.mission.domain.MissionItemProjectFinancer;
import module.mission.domain.MissionProcess;
import module.mission.domain.activity.FundAllocationActivityInformation.MissionItemFinancerFundAllocationBean;
import module.workflow.activities.ActivityInformation;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

public class AllocateProjectFundsActivity extends MissionProcessActivity<MissionProcess, AllocateProjectFundsActivityInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return super.isActive(missionProcess, user) && missionProcess.isApproved()
                && !missionProcess.getIsCanceled().booleanValue() && !missionProcess.hasAllAllocatedProjectFunds()
                && missionProcess.canAllocateProjectFund();
    }

    @Override
    protected void process(final AllocateProjectFundsActivityInformation allocateFundsActivityInformation) {
        final Boolean requirePriorFundAllocation =
                ExpenditureTrackingSystem.getInstance().getRequireFundAllocationPriorToAcquisitionRequest();
        for (final MissionItemFinancerFundAllocationBean missionItemFinancerFundAllocationBean : allocateFundsActivityInformation
                .getMissionItemFinancerFundAllocationBeans()) {
            final MissionItemProjectFinancer missionItemFinancer =
                    (MissionItemProjectFinancer) missionItemFinancerFundAllocationBean.getMissionItemFinancer();
            final String fundAllocationId = missionItemFinancerFundAllocationBean.getFundAllocationId();
            missionItemFinancer.setProjectFundAllocationId(fundAllocationId);
            if (requirePriorFundAllocation != null && !requirePriorFundAllocation.booleanValue()) {
                missionItemFinancer.setFundAllocationId(" ");
            }
        }
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
        return new AllocateProjectFundsActivityInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

}
