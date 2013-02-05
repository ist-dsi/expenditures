package module.mission.domain.activity;

import java.io.Serializable;

import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public class SendForProcessTerminationWithChangesActivityInformation extends ActivityInformation<MissionProcess> implements
        Serializable {

    private String descriptionOfChangesAfterArrival;

    public SendForProcessTerminationWithChangesActivityInformation(final MissionProcess missionProcess,
            final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
        super(missionProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
        return super.hasAllneededInfo() && descriptionOfChangesAfterArrival != null
                && !descriptionOfChangesAfterArrival.isEmpty();
    }

    public String getDescriptionOfChangesAfterArrival() {
        return descriptionOfChangesAfterArrival;
    }

    public void setDescriptionOfChangesAfterArrival(String descriptionOfChangesAfterArrival) {
        this.descriptionOfChangesAfterArrival = descriptionOfChangesAfterArrival;
    }

}
