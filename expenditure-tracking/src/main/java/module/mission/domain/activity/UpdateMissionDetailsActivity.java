package module.mission.domain.activity;

import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class UpdateMissionDetailsActivity extends MissionProcessActivity<MissionProcess, UpdateMissionDetailsActivityInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return super.isActive(missionProcess, user) && (missionProcess.isUnderConstruction() && missionProcess.isRequestor(user));
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
