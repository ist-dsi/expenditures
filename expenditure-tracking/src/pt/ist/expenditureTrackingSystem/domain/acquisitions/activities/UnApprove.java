package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;

public class UnApprove<T extends PaymentProcess> extends AbstractActivity<T> {

    @Override
    protected boolean isAccessible(final T process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && process.isResponsibleForUnit(loggedPerson);
    }

    @Override
    protected boolean isAvailable(final T process) {
	final Person loggedPerson = getLoggedPerson();
	return isCurrentUserProcessOwner(process)
		&& (process.isPendingApproval() || process.isInApprovedState())
		&& process.getRequest().hasBeenApprovedBy(loggedPerson);
    }

    @Override
    protected void process(final T process, final Object... objects) {
	final Person loggedPerson = getLoggedPerson();
	process.getRequest().unSubmitForFundsAllocation(loggedPerson);
	process.submitForApproval();
    }

}
