package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.fenixWebFramework.security.UserView;

public class UnApprove<T extends PaymentProcess> extends AbstractActivity<T> {

    @Override
    protected boolean isAccessible(final T process) {
	final User user = UserView.getUser();
	return user != null && process.isResponsibleForUnit(user.getPerson());
    }

    @Override
    protected boolean isAvailable(final T process) {
	final User user = UserView.getUser();
	final Person person = user.getPerson();
	return isCurrentUserProcessOwner(process)
		&& (process.isPendingApproval() || process.isInApprovedState())
		&& process.getRequest().hasBeenApprovedBy(person);
    }

    @Override
    protected void process(final T process, final Object... objects) {
	final User user = UserView.getUser();
	final Person person = user.getPerson();
	process.getRequest().unSubmitForFundsAllocation(person);
	process.submitForApproval();
    }

}
