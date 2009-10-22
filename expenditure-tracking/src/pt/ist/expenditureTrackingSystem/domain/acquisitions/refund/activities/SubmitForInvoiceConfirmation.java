package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class SubmitForInvoiceConfirmation extends GenericRefundProcessActivity {

    @Override
    protected boolean isAvailable(RefundProcess process) {
	return isCurrentUserRequestor(process);
    }

    @Override
    protected boolean isAccessible(RefundProcess process) {
	return isCurrentUserProcessOwner(process) && process.isInAuthorizedState() && !process.getRefundableInvoices().isEmpty()
		&& isRealValueFullyAttributeToItems(process.getRequest().getRefundItemsSet());
    }

    private boolean isRealValueFullyAttributeToItems(Set<RefundItem> requestItems) {
	for (RefundItem item : requestItems) {
	    if (item.isAnyRefundInvoiceAvailable() && !item.isRealValueFullyAttributedToUnits()) {
		return false;
	    }
	}
	return true;
    }

    @Override
    protected void process(RefundProcess process, Object... objects) {
	process.submitForInvoiceConfirmation();
    }

}
