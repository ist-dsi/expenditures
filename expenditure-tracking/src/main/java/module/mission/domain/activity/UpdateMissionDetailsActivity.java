package module.mission.domain.activity;

import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;

public class UpdateMissionDetailsActivity extends MissionProcessActivity<MissionProcess, UpdateMissionDetailsActivityInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return super.isActive(missionProcess, user) && (missionProcess.isUnderConstruction() && missionProcess.isRequestor(user)
//		|| (missionProcess.isTerminatedWithChanges()
//			&& !missionProcess.isArchived()
//			&& missionProcess.canArchiveMission())
                );
    }

    @Override
    protected void process(final UpdateMissionDetailsActivityInformation updateMissionDetailsActivityInformation) {
        final MissionProcess missionProcess = updateMissionDetailsActivityInformation.getProcess();
        final Mission mission = missionProcess.getMission();
        mission.updateDetails(updateMissionDetailsActivityInformation);
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
        return new UpdateMissionDetailsActivityInformation(process, this);
    }
}
