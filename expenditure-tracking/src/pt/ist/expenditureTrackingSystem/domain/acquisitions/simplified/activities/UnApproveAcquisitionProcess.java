package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

public class UnApproveAcquisitionProcess extends
	WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	return isUserProcessOwner(process, user) && process.getFundAllocationExpirationDate() == null
		&& process.getAcquisitionRequest().hasBeenApprovedBy(user.getExpenditurePerson());
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
	RegularAcquisitionProcess process = activityInformation.getProcess();
	process.getAcquisitionRequest().unapprove(UserView.getCurrentUser().getExpenditurePerson());
	process.submitForApproval();

    }

}
