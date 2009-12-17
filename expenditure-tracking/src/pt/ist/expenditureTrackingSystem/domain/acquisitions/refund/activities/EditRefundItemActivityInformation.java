package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class EditRefundItemActivityInformation extends CreateRefundItemActivityInformation {

    private RefundItem item;

    public EditRefundItemActivityInformation(RefundProcess refundProcess,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(refundProcess, activity);
    }

    public RefundItem getItem() {
	return item;
    }

    public void setItem(RefundItem item) {
	this.item = item;
	setValueEstimation(item.getValueEstimation());
	setCPVReference(item.getCPVReference());
	setDescription(item.getDescription());
    }

    @Override
    public boolean hasAllneededInfo() {
	return super.hasAllneededInfo() && isForwardedFromInput() && getItem() != null;
    }
}
