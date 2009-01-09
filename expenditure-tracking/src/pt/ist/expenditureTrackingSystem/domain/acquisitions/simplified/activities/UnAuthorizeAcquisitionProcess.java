package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.security.UserView;

public class UnAuthorizeAcquisitionProcess extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	User user = getUser();
	return user != null
		&& process.isResponsibleForUnit(user.getPerson(), process.getAcquisitionRequest().getTotalItemValueWithAdditionalCostsAndVat());
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	final User user = UserView.getUser();
	final Person person = user.getPerson();
	final AcquisitionRequest acquisitionRequest = process.getRequest();
	return super.isAvailable(process)
		&& acquisitionRequest.hasBeenApprovedBy(person)
		&& (process.getAcquisitionProcessState().isInAllocatedToUnitState() || process.isInAuthorizedState());
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	final User user = UserView.getUser();
	final Person person = user.getPerson();
	process.getAcquisitionRequest().unapproveBy(person);
	process.allocateFundsToUnit();
    }

}
