package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.fenixWebFramework.security.UserView;

public class UnSubmitForApproval extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	User user = UserView.getUser();
	return user != null && ( user.getPerson().equals(process.getRequestor()) || process.isResponsibleForUnit(user.getPerson()));
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return  super.isAvailable(process) && process.getAcquisitionProcessState().isPendingApproval() && !process.getAcquisitionRequest().isApprovedByAtLeastOneResponsible();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	process.getAcquisitionRequest().unSubmitForFundsAllocation();
	process.inGenesis();
    }

}
