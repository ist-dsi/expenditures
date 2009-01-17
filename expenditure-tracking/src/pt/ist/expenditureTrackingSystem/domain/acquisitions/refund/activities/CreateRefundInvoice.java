package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.RefundInvoiceBean;

public class CreateRefundInvoice extends GenericRefundProcessActivity {

    @Override
    protected boolean isAvailable(RefundProcess process) {
	return isCurrentUserRequestor(process);
    }

    @Override
    protected boolean isAccessible(RefundProcess process) {
	return isCurrentUserProcessOwner(process) && process.isInAuthorizedState();
    }

    @Override
    protected void process(RefundProcess process, Object... objects) {
	RefundInvoiceBean bean = (RefundInvoiceBean) objects[0];
	byte[] filesBytes = (byte[]) objects[1];
	RefundItem item = bean.getItem();
	item.createRefundInvoice(bean.getInvoiceNumber(), bean.getInvoiceDate(), bean.getValue(), bean.getVatValue(), bean
		.getRefundableValue(), filesBytes, bean.getFilename(), item, bean.getSupplier());
    }

}
