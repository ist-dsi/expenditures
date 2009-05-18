package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class Approve extends GenericRefundProcessActivity {

    @Override
    protected boolean isAccessible(final RefundProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null
		&& process.isResponsibleForUnit(loggedPerson)
		&& !process.getRequest().hasBeenApprovedBy(loggedPerson);
    }

    @Override
    protected boolean isAvailable(final RefundProcess process) {
	return super.isAvailable(process) && process.isPendingApproval();
    }

    @Override
    protected void process(final RefundProcess process, final Object... objects) {
	final Person person = getLoggedPerson();
	process.getRequest().approve(person);
	if (process.getRequest().isSubmittedForFundsAllocationByAllResponsibles()) {
	    process.submitForFundAllocation();
	}
    }

}
