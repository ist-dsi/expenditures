package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestWithPayment;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.fenixWebFramework.security.UserView;

public class UnAuthorize<T extends PaymentProcess> extends AbstractActivity<T> {

    @Override
    protected boolean isAccessible(final T process) {
	User user = getUser();
	return user != null
		&& process.isResponsibleForUnit(user.getPerson(), process.getRequest().getTotalValue());
    }

    @Override
    protected boolean isAvailable(final T process) {
	final User user = UserView.getUser();
	final Person person = user.getPerson();
	final RequestWithPayment requestWithPayment = process.getRequest();
	return isCurrentUserProcessOwner(process)
		&& requestWithPayment.hasBeenAuthorizedBy(person)
		&& (process.isInAllocatedToUnitState() || process.isInAuthorizedState());
    }

    @Override
    protected void process(final T process, final Object... objects) {
	final User user = UserView.getUser();
	final Person person = user.getPerson();
	process.getRequest().unathorizeBy(person);
	process.allocateFundsToUnit();
    }

}
