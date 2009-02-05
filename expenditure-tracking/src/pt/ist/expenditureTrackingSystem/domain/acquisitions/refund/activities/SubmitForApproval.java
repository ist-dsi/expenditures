package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundRequest;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class SubmitForApproval extends GenericRefundProcessActivity {

    @Override
    protected boolean isAccessible(final RefundProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && loggedPerson == process.getRequestor();
    }

    @Override
    protected boolean isAvailable(final RefundProcess process) {
	final RefundRequest refundRequest = process.getRequest();
	return super.isAvailable(process) && process.getProcessState().isInGenesis() && refundRequest.hasAnyRequestItems()
		&& refundRequest.isEveryItemFullyAttributedToPayingUnits();
    }

    @Override
    protected void process(final RefundProcess process, final Object... objects) {
	process.submitForApproval();
    }

}
