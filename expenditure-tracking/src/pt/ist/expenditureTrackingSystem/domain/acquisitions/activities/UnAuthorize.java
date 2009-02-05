package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestWithPayment;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;

public class UnAuthorize<T extends PaymentProcess> extends AbstractActivity<T> {

    @Override
    protected boolean isAccessible(final T process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null
		&& process.isResponsibleForUnit(loggedPerson, process.getRequest().getTotalValue());
    }

    @Override
    protected boolean isAvailable(final T process) {
	final Person loggedPerson = getLoggedPerson();
	final RequestWithPayment requestWithPayment = process.getRequest();
	return isCurrentUserProcessOwner(process)
		&& requestWithPayment.hasBeenAuthorizedBy(loggedPerson)
		&& (process.isInAllocatedToUnitState() || process.isAuthorized());
    }

    @Override
    protected void process(final T process, final Object... objects) {
	final Person loggedPerson = getLoggedPerson();
	process.getRequest().unathorizeBy(loggedPerson);
	process.allocateFundsToUnit();
    }

}
