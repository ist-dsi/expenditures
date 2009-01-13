package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.security.UserView;

public class UnApproveAcquisitionProcess extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	final User user = UserView.getUser();
	return user != null && process.isResponsibleForUnit(user.getPerson());
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	final User user = UserView.getUser();
	final Person person = user.getPerson();
	return super.isAvailable(process) && process.getFundAllocationExpirationDate() == null
		&& process.getAcquisitionRequest().hasBeenSubmittedForFundsAllocationBy(person);
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	final User user = UserView.getUser();
	final Person person = user.getPerson();
	process.getAcquisitionRequest().unSubmitForFundsAllocation(person);
	process.submitForApproval();
    }

}
