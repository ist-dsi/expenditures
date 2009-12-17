package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import java.util.Set;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class SubmitForInvoiceConfirmation extends WorkflowActivity<RefundProcess, ActivityInformation<RefundProcess>> {

    private boolean isRealValueFullyAttributeToItems(Set<RefundItem> requestItems) {
	for (RefundItem item : requestItems) {
	    if (item.isAnyRefundInvoiceAvailable() && !item.isRealValueFullyAttributedToUnits()) {
		return false;
	    }
	}
	return true;
    }

    @Override
    public boolean isActive(RefundProcess process, User user) {
	return process.getRequestor() == user.getExpenditurePerson() && isUserProcessOwner(process, user)
		&& process.isInAuthorizedState() && !process.getRefundableInvoices().isEmpty()
		&& isRealValueFullyAttributeToItems(process.getRequest().getRefundItemsSet());
    }

    @Override
    protected void process(ActivityInformation<RefundProcess> activityInformation) {
	activityInformation.getProcess().submitForInvoiceConfirmation();
    }

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
	return "resources/AcquisitionResources";
    }

}
