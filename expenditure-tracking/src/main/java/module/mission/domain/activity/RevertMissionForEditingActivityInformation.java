package module.mission.domain.activity;

import java.io.Serializable;

import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public class RevertMissionForEditingActivityInformation extends ActivityInformation<MissionProcess> implements Serializable {

    private String description;

    public RevertMissionForEditingActivityInformation(final MissionProcess missionProcess,
            final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
        super(missionProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
        return super.hasAllneededInfo() && description != null && !description.trim().isEmpty() && isForwardedFromInput();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

}
