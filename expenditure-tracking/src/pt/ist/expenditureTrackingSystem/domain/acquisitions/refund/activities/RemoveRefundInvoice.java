package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundableInvoiceFile;

public class RemoveRefundInvoice extends GenericRefundProcessActivity {

    @Override
    protected boolean isAccessible(RefundProcess process) {
	return isCurrentUserRequestor(process);
    }

    @Override
    protected boolean isAvailable(RefundProcess process) {
	return (isCurrentUserProcessOwner(process) && process.isInAuthorizedState() && process.isAnyRefundInvoiceAvailable());
    }

    @Override
    protected void process(RefundProcess process, Object... objects) {
	RefundableInvoiceFile invoice = (RefundableInvoiceFile) objects[0];
	invoice.delete();
    }

}
