package module.mission.domain.activity;

import java.io.Serializable;
import java.util.Set;

import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;
import module.mission.domain.RemoteMissionSystem;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public class AssociateMissionProcessActivityInfo extends ActivityInformation<MissionProcess> implements Serializable {

    private RemoteMissionSystem remoteMissionSystem;
    private String processNumber;
    private String externalId;
    private boolean connect = true;

    public AssociateMissionProcessActivityInfo(final MissionProcess missionProcess,
            final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
        super(missionProcess, activity);
    }

    public Set<MissionSystem> getAllMissionSystems() {
        return MissionSystem.readAllMissionSystems();
    }

    @Override
    public boolean hasAllneededInfo() {
        return remoteMissionSystem != null && processNumber != null && !processNumber.isEmpty();
    }

    public String getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(String processNumber) {
        this.processNumber = processNumber;
    }

    public RemoteMissionSystem getRemoteMissionSystem() {
        return remoteMissionSystem;
    }

    public void setRemoteMissionSystem(RemoteMissionSystem remoteMissionSystem) {
        this.remoteMissionSystem = remoteMissionSystem;
    }

    public boolean isConnect() {
        return connect;
    }

    public void setConnect(boolean connect) {
        this.connect = connect;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

}
