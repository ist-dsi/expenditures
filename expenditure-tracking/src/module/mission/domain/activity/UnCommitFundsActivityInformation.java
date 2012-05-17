package module.mission.domain.activity;

import java.io.Serializable;

import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public class UnCommitFundsActivityInformation extends ActivityInformation<MissionProcess> implements Serializable {

    private static final long serialVersionUID = 1L;

    private MissionFinancer missionFinancer;

    public UnCommitFundsActivityInformation(final MissionProcess missionProcess,
	    final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
	super(missionProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
	return missionFinancer != null;
    }

    public MissionFinancer getMissionFinancer() {
        return missionFinancer;
    }

    public void setMissionFinancer(MissionFinancer missionFinancer) {
        this.missionFinancer = missionFinancer;
    }

}
