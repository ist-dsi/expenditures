package module.mission.domain.activity;

import java.io.Serializable;

import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public class RemoveFinancerActivityInformation extends ActivityInformation<MissionProcess> implements Serializable {

	private MissionFinancer financer;

	public RemoveFinancerActivityInformation(final MissionProcess missionProcess,
			final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
		super(missionProcess, activity);
	}

	@Override
	public boolean hasAllneededInfo() {
		return getFinancer() != null;
	}

	public MissionFinancer getFinancer() {
		return financer;
	}

	public void setFinancer(final MissionFinancer financer) {
		this.financer = financer;
	}

}
