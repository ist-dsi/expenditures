package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;

public class Authorize<T extends PaymentProcess> extends AbstractActivity<T> {

    @Override
    protected boolean isAccessible(final T process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null
		&& process.isResponsibleForUnit(loggedPerson, process.getRequest().getTotalValue())
		&& !process.getRequest().hasBeenAuthorizedBy(loggedPerson);
    }

    @Override
    protected boolean isAvailable(final T process) {
	return isCurrentUserProcessOwner(process)
		&& process.isInAllocatedToUnitState();
    }

    @Override
    protected void process(final T process, final Object... objects) {
	final Person loggedPerson = getLoggedPerson();
	process.authorizeBy(loggedPerson);
    }

}
