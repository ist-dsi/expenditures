package module.mission.domain.activity;

import module.mission.domain.Mission;
import module.mission.domain.MissionChangeDescription;
import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionProcess;
import module.mission.domain.MissionVersion;
import module.mission.domain.PersonMissionAuthorization;
import module.mission.domain.VehiclItem;
import module.workflow.activities.ActivityInformation;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;

public class RevertMissionForEditingActivity extends
        MissionProcessActivity<MissionProcess, RevertMissionForEditingActivityInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(MissionProcess missionProcess, User user) {
        return super.isActive(missionProcess, user) && !missionProcess.isUnderConstruction() && !missionProcess.isCanceled()
                && missionProcess.isRequestor(user) && !missionProcess.getMission().getMissionVersion().isTerminated()
                && !missionProcess.isPersonalInformationProcessed();
    }

    @Override
    protected void process(RevertMissionForEditingActivityInformation activityInformation) {
        MissionProcess missionProcess = activityInformation.getProcess();
        Mission mission = missionProcess.getMission();
        new MissionVersion(mission);
        new MissionChangeDescription(mission, activityInformation.getDescription());

        for (MissionFinancer missionFinancer : mission.getFinancerSet()) {
            missionFinancer.clearFundAllocations();
            missionFinancer.setCommitmentNumber(null);
            missionFinancer.setAuthorization(null);
            missionFinancer.setApproval(null);
        }
        for (VehiclItem vehicle : mission.getVehicleItems()) {
            vehicle.setAuthorized(false);
        }
        for (PersonMissionAuthorization personAuthorization : mission.getPersonMissionAuthorizationsSet()) {
            personAuthorization.clearAuthorities();
        }

        mission.setApprovalForMissionWithNoFinancers(null);
        mission.setIsApprovedByMissionResponsible(null);
        mission.setIsVerified(false);
        mission.setIsPersonalInformationProcessed(false);

        missionProcess.removeFromVerificationQueue();
        missionProcess.removeFromParticipantInformationQueues();
        missionProcess.setIsUnderConstruction(Boolean.TRUE);
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(MissionProcess process) {
        return new RevertMissionForEditingActivityInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

}
