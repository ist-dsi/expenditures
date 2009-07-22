package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class RevertInvoiceConfirmationSubmition extends GenericRefundProcessActivity {

    @Override
    protected boolean isAvailable(RefundProcess process) {
	return isCurrentUserRequestor(process);
    }

    @Override
    protected boolean isAccessible(RefundProcess process) {
	return isCurrentUserProcessOwner(process) && process.isInSubmittedForInvoiceConfirmationState();
    }

    @Override
    protected void process(RefundProcess process, Object... objects) {
	process.revertInvoiceConfirmationSubmition();
    }

}
