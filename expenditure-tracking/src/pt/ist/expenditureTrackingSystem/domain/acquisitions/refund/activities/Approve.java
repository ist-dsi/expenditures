package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class Approve extends GenericRefundProcessActivity {

    @Override
    protected boolean isAccessible(final RefundProcess process) {
	User user = getUser();
	return user != null
		&& process.isResponsibleForUnit(user.getPerson())
		&& !process.getRequest().hasBeenApprovedBy(user.getPerson());
    }

    @Override
    protected boolean isAvailable(final RefundProcess process) {
	return super.isAvailable(process) && process.isPendingApproval();
    }

    @Override
    protected void process(final RefundProcess process, final Object... objects) {
	final User user = getUser();
	final Person person = user.getPerson();
	process.getRequest().submittedForFundsAllocation(person);
	if (process.getRequest().isSubmittedForFundsAllocationByAllResponsibles()) {
	    process.submitForFundAllocation();
	}
    }

}
