package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class UnSubmitForFundAllocation extends GenericRefundProcessActivity {

    @Override
    protected boolean isAccessible(RefundProcess process) {
	return process.isAccountingEmployee() || process.isProjectAccountingEmployee();
    }

    @Override
    protected boolean isAvailable(RefundProcess process) {
	return isCurrentUserProcessOwner(process) && process.isPendingFundAllocation() && !process.hasAnyAllocatedFunds();
    }

    @Override
    protected void process(RefundProcess process, Object... objects) {
	process.unApproveByAll();
	process.submitForApproval();
    }

}
