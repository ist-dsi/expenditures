package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundableInvoiceFile;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;

public class RemoveRefundInvoiceActivityInformation extends ActivityInformation<RefundProcess> {

    RefundableInvoiceFile refundInvoice;

    public RemoveRefundInvoiceActivityInformation(RefundProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    public RefundableInvoiceFile getRefundInvoice() {
	return refundInvoice;
    }

    public void setRefundInvoice(RefundableInvoiceFile refundInvoice) {
	this.refundInvoice = refundInvoice;
    }

    @Override
    public boolean hasAllneededInfo() {
	return getRefundInvoice() != null;
    }
}
