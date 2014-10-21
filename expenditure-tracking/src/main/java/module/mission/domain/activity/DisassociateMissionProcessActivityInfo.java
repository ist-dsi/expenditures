package module.mission.domain.activity;

import java.io.Serializable;
import java.util.Set;

import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;
import module.mission.domain.RemoteMissionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public class DisassociateMissionProcessActivityInfo extends ActivityInformation<MissionProcess> implements Serializable {

    private RemoteMissionProcess remoteMissionProcess;
    private boolean connect;

    public DisassociateMissionProcessActivityInfo(final MissionProcess missionProcess,
            final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
        super(missionProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
        return remoteMissionProcess != null;
    }

    public RemoteMissionProcess getRemoteMissionProcess() {
        return remoteMissionProcess;
    }

    public void setRemoteMissionProcess(RemoteMissionProcess remoteMissionProcess) {
        this.remoteMissionProcess = remoteMissionProcess;
    }

    public boolean isConnect() {
        return connect;
    }

    public void setConnect(boolean connect) {
        this.connect = connect;
    }

}
