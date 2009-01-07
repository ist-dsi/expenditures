package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class DeleteRefundItem extends GenericRefundProcessActivity {

    @Override
    protected boolean isAccessible(RefundProcess process) {
	return isCurrentUserRequestor(process);
    }

    @Override
    protected boolean isAvailable(RefundProcess process) {
	return super.isAvailable(process) && process.isInGenesis();
    }

    @Override
    protected void process(RefundProcess process, Object... objects) {
	RefundItem item = (RefundItem) objects[0];
	item.delete();
    }
}
