package module.mission.domain.activity;

import java.io.Serializable;
import java.util.Set;

import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public class AssociateMissionProcessActivityInfo extends ActivityInformation<MissionProcess> implements Serializable {

    private MissionProcess missionProcessToAssociate;

    public AssociateMissionProcessActivityInfo(final MissionProcess missionProcess,
            final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
        super(missionProcess, activity);
    }

    public Set<MissionSystem> getAllMissionSystems() {
        return MissionSystem.readAllMissionSystems();
    }

    @Override
    public boolean hasAllneededInfo() {
        return getMissionProcessToAssociate() != null;
    }

    public MissionProcess getMissionProcessToAssociate() {
        return missionProcessToAssociate;
    }

    public void setMissionProcessToAssociate(MissionProcess missionProcessToAssociate) {
        this.missionProcessToAssociate = missionProcessToAssociate;
    }
}
