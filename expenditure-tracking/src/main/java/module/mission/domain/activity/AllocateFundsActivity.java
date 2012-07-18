package module.mission.domain.activity;

import module.mission.domain.MissionItemFinancer;
import module.mission.domain.MissionProcess;
import module.mission.domain.activity.FundAllocationActivityInformation.MissionItemFinancerFundAllocationBean;
import module.workflow.activities.ActivityInformation;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;

public class AllocateFundsActivity extends MissionProcessActivity<MissionProcess, AllocateFundsActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
	return super.isActive(missionProcess, user)
		&& missionProcess.isApproved()
		&& !missionProcess.getIsCanceled().booleanValue()
		&& (!missionProcess.hasAnyProjectFinancer() || missionProcess.hasAllAllocatedProjectFunds())
		&& !missionProcess.hasAllAllocatedFunds()
		&& missionProcess.canAllocateFund();
    }

    @Override
    protected void process(final AllocateFundsActivityInformation allocateFundsActivityInformation) {
	for (final MissionItemFinancerFundAllocationBean missionItemFinancerFundAllocationBean : allocateFundsActivityInformation.getMissionItemFinancerFundAllocationBeans()) {
	    final MissionItemFinancer missionItemFinancer = missionItemFinancerFundAllocationBean.getMissionItemFinancer();
	    final String fundAllocationId = missionItemFinancerFundAllocationBean.getFundAllocationId();
	    missionItemFinancer.setFundAllocationId(fundAllocationId);
	}
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
	return new AllocateFundsActivityInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
	return false;
    }

}
