package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;

public abstract class GenericRefundProcessActivity extends AbstractActivity<RefundProcess> {

    public boolean isCurrentUserRequestor(RefundProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && process.getRequestor() == loggedPerson;
    }

    protected boolean isCurrentUserProcessOwner(RefundProcess process) {
	final User currentOwner = process.getCurrentOwner();
	return currentOwner == null || process.isTakenByCurrentUser();
    }

    @Override
    protected boolean isAvailable(RefundProcess process) {
	return isCurrentUserProcessOwner(process);
    }
}
