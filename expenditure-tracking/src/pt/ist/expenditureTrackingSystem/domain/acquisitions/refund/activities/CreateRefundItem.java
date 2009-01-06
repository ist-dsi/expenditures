package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.RefundItemBean;

public class CreateRefundItem extends GenericRefundProcessActivity {

    @Override
    protected boolean isAccessible(RefundProcess process) {
	return isCurrentUserRequestor(process);
    }

    @Override
    protected boolean isAvailable(RefundProcess process) {
	return process.isInGenesis();
    }

    @Override
    protected void process(RefundProcess process, Object... objects) {
	RefundItemBean bean = (RefundItemBean) objects[0];
	process.getRequest().createRefundItem(bean);
    }

}
