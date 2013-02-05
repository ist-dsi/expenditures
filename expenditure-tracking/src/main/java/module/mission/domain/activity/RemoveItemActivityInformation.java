package module.mission.domain.activity;

import java.io.Serializable;

import module.mission.domain.MissionItem;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public class RemoveItemActivityInformation extends ActivityInformation<MissionProcess> implements Serializable {

    private MissionItem missionItem;

    public RemoveItemActivityInformation(final MissionProcess missionProcess,
            final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
        super(missionProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
        return missionItem != null;
    }

    public MissionItem getMissionItem() {
        return missionItem;
    }

    public void setMissionItem(final MissionItem missionItem) {
        this.missionItem = missionItem;
    }

}
