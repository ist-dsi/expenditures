package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.fenixWebFramework.security.UserView;

public class Authorize<T extends PaymentProcess> extends AbstractActivity<T> {

    @Override
    protected boolean isAccessible(final T process) {
	User user = getUser();
	return user != null
		&& process.isResponsibleForUnit(user.getPerson(), process.getRequest().getTotalValue())
		&& !process.getRequest().hasBeenAuthorizedBy(user.getPerson());
    }

    @Override
    protected boolean isAvailable(final T process) {
	return isCurrentUserProcessOwner(process)
		&& process.isInAllocatedToUnitState();
    }

    @Override
    protected void process(final T process, final Object... objects) {
	final User user = UserView.getUser();
	final Person person = user.getPerson();
	process.authorizeBy(person);
    }

}
