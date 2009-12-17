package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class DeleteRefundItemActivityInformation extends ActivityInformation<RefundProcess> {

    private RefundItem item;

    public DeleteRefundItemActivityInformation(RefundProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    public RefundItem getItem() {
	return item;
    }

    public void setItem(RefundItem item) {
	this.item = item;
    }
}
