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
	return isCurrentUserProcessOwner(process) && process.isInAllocatedToUnitState()
		&& process.hasAllocatedFundsForAllProjectFinancers() && process.hasAllFundAllocationId(getLoggedPerson());
    }

    @Override
    protected void process(RefundProcess process, Object... objects) {
	process.getRequest().resetFundAllocationId(getLoggedPerson());
	process.submitForFundAllocation();
    }

}
