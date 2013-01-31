package module.mission.domain.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import module.mission.domain.Mission;
import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionItem;
import module.mission.domain.MissionItemFinancer;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public abstract class FundAllocationActivityInformation extends ActivityInformation<MissionProcess> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static class MissionItemFinancerFundAllocationBean implements Serializable {
		private static final long serialVersionUID = 1L;

		private final MissionItemFinancer missionItemFinancer;
		private String fundAllocationId;

		public MissionItemFinancerFundAllocationBean(final MissionItemFinancer missionItemFinancer) {
			this.missionItemFinancer = missionItemFinancer;
		}

		public String getFundAllocationId() {
			return fundAllocationId;
		}

		public void setFundAllocationId(String fundAllocationId) {
			this.fundAllocationId = fundAllocationId;
		}

		public MissionItemFinancer getMissionItemFinancer() {
			return missionItemFinancer;
		}

	}

	protected abstract boolean canAllocateFunds(final MissionFinancer missionFinancer);

	private final Collection<MissionItemFinancerFundAllocationBean> missionItemFinancerFundAllocationBeans =
			new ArrayList<MissionItemFinancerFundAllocationBean>();

	public FundAllocationActivityInformation(final MissionProcess missionProcess,
			final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
		super(missionProcess, activity);
		final Mission mission = missionProcess.getMission();
		for (final MissionFinancer financer : mission.getFinancerSet()) {
			if (canAllocateFunds(financer)) {
				for (final MissionItemFinancer missionItemFinancer : financer.getMissionItemFinancersSet()) {
					final MissionItem missionItem = missionItemFinancer.getMissionItem();
					if (!checkIfRequiresFundAllocation() || missionItem.requiresFundAllocation()) {
						missionItemFinancerFundAllocationBeans
								.add(new MissionItemFinancerFundAllocationBean(missionItemFinancer));
					}
				}
			}
		}
	}

	public Collection<MissionItemFinancerFundAllocationBean> getMissionItemFinancerFundAllocationBeans() {
		return missionItemFinancerFundAllocationBeans;
	}

	@Override
	public boolean hasAllneededInfo() {
		return isForwardedFromInput();
	}

	protected boolean checkIfRequiresFundAllocation() {
		return true;
	}

}
