package module.mission.domain.util;

import module.mission.domain.MissionProcess;
import module.mission.domain.MissionYear;
import module.mission.domain.NationalMissionProcess;
import module.workflow.domain.ProcessCounter;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;

public class MissionPendingProcessCounter extends ProcessCounter {

    public MissionPendingProcessCounter() {
	super(MissionProcess.class);
    }

    @Override
    public Class getProcessClassForForwarding() {
        return NationalMissionProcess.class;
    }

    @Override
    public int getCount() {
	final MissionYear missionYear = MissionYear.getCurrentYear();

	final int takenByUser = missionYear.getTaken().size();
	final int pendingApprovalCount = missionYear.getPendingAproval().size();
	final int pendingAuthorizationCount = missionYear.getPendingAuthorization().size();
	final int pendingFundAllocationCount = missionYear.getPendingFundAllocation().size();
	final int pendingProcessingCount = missionYear.getPendingProcessingPersonelInformation().size();

	return takenByUser + pendingApprovalCount + pendingAuthorizationCount + pendingFundAllocationCount + pendingProcessingCount;
    }

}
