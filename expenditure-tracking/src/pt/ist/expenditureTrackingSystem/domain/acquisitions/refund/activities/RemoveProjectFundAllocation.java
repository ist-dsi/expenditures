package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class RemoveProjectFundAllocation extends GenericRefundProcessActivity {
    @Override
    protected boolean isAccessible(RefundProcess process) {
	return process.isProjectAccountingEmployee();
    }

    @Override
    protected boolean isAvailable(RefundProcess process) {
	return isCurrentUserProcessOwner(process) && process.hasAllocatedFundsForAllProjectFinancers(getUser().getPerson());
    }

    @Override
    protected void process(RefundProcess process, Object... objects) {
	process.getRequest().resetProjectFundAllocationId(getUser().getPerson());
	if (!process.getRequest().hasAllFundAllocationId()) {
	    process.unApproveByAll();
	    process.submitForApproval();
	}
    }
}
