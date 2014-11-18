package module.mission.domain.activity;

import module.mission.domain.Mission;
import module.mission.domain.MissionItem;
import module.mission.domain.MissionProcess;
import module.mission.domain.MissionVersion;
import module.workflow.activities.ActivityInformation;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class ArchiveItemActivity extends MissionProcessActivity<MissionProcess, ActivityInformation<MissionProcess>> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return super.isActive(missionProcess, user) && missionProcess.isTerminated() && !missionProcess.isArchived()
                && missionProcess.canArchiveMission();
    }

    @Override
    protected void process(final ActivityInformation<MissionProcess> activityInformation) {
        final MissionProcess process = activityInformation.getProcess();
        final Mission mission = process.getMission();
        final MissionVersion missionVersion = mission.getMissionVersion();
        for (final MissionItem missionItem : missionVersion.getMissionItemsSet()) {
            if (!missionItem.isArchived()) {
                missionItem.archive();
            }
        }
        if (areAllMissionItemFinancersArchived(missionVersion)) {
            missionVersion.setIsArchived(Boolean.TRUE);
//            if (missionVersion.getChangesAfterArrival().booleanValue()) {
            process.addToProcessParticipantInformationQueues();
//            }
        }
    }

    private boolean areAllMissionItemFinancersArchived(final MissionVersion missionVersion) {
        for (final MissionItem missionItem : missionVersion.getMissionItemsSet()) {
            if (!missionItem.isArchived()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
        return new ActivityInformation(process, this);
    }

    @Override
    public boolean isConfirmationNeeded(MissionProcess process) {
        return true;
    }

    @Override
    public String getLocalizedConfirmationMessage() {
        return BundleUtil.getString("resources/MissionResources", "label.module.mission.archive.confirm");
    }

}
