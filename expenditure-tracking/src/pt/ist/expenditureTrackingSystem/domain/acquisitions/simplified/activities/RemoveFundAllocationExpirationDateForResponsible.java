package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.security.UserView;

public class RemoveFundAllocationExpirationDateForResponsible extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	final User user = UserView.getUser();
	return user != null && process.isResponsibleForUnit(user.getPerson());
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {

	return super.isAvailable(process) && process.getFundAllocationExpirationDate() != null && !process.hasAnyAllocatedFunds();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	final User user = UserView.getUser();
	final Person person = user.getPerson();
	process.setFundAllocationExpirationDate(null);
	process.getAcquisitionRequest().unathorizeBy(person);
	process.submitForApproval();
	new UnApproveAcquisitionProcess().execute(process, objects);
    }

}
