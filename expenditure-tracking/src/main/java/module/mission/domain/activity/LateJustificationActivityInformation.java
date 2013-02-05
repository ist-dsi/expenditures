package module.mission.domain.activity;

import java.io.Serializable;

import module.mission.domain.MissionProcess;
import module.mission.domain.MissionProcessLateJustification;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public class LateJustificationActivityInformation extends ActivityInformation<MissionProcess> implements Serializable {

    private String justification;

    public LateJustificationActivityInformation(final MissionProcess missionProcess,
            final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
        super(missionProcess, activity);
        final MissionProcessLateJustification justification = missionProcess.getLastMissionProcessLateJustification();
        if (justification != null) {
            this.justification = justification.getJustification();
        }
    }

    @Override
    public boolean hasAllneededInfo() {
        final MissionProcess missionProcess = getProcess();
        return missionProcess.isOnTime() || (isForwardedFromInput() && justification != null && !justification.isEmpty());
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(final String justification) {
        this.justification = justification;
    }

}
