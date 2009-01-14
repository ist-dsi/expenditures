package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class RemoveFundAllocation extends GenericRefundProcessActivity {

    @Override
    protected boolean isAccessible(RefundProcess process) {
	return process.isAccountingEmployee();
    }

    @Override
    protected boolean isAvailable(RefundProcess process) {
	return isCurrentUserProcessOwner(process) && process.isPendingFundAllocation()
		&& process.hasAllocatedFundsForAllProjectFinancers() && !process.hasAllFundAllocationId(getUser().getPerson());
    }

    @Override
    protected void process(RefundProcess process, Object... objects) {
	process.getRequest().resetFundAllocationId(getUser().getPerson());
	if (!process.getRequest().hasAllFundAllocationId()) {
	    process.unApproveByAll();
	    process.submitForApproval();
	}
    }

}
