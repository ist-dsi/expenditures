package module.mission.domain.activity;

import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class UpdateForeignMissionDetailsActivity extends
        MissionProcessActivity<MissionProcess, UpdateForeignMissionDetailsActivityInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return super.isActive(missionProcess, user) && (missionProcess.isUnderConstruction() && missionProcess.isRequestor(user));
    }

    @Override
    protected void process(final UpdateForeignMissionDetailsActivityInformation updateForeignMissionDetailsActivityInformation) {
        final MissionProcess missionProcess = updateForeignMissionDetailsActivityInformation.getProcess();
        final Mission mission = missionProcess.getMission();
        mission.updateDetails(updateForeignMissionDetailsActivityInformation);
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
        return new UpdateForeignMissionDetailsActivityInformation(process, this);
    }
}
