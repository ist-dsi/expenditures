package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.AbstractChangeFinancersAccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class ChangeFinancersAccountingUnit extends AbstractChangeFinancersAccountingUnit<RefundProcess> {

    @Override
    protected boolean isAccessible(RefundProcess process) {
	return process.isAccountingEmployeeForOnePossibleUnit() || process.isProjectAccountingEmployeeForOnePossibleUnit();
    }

    @Override
    protected boolean isAvailable(RefundProcess process) {
	return isCurrentUserProcessOwner(process) && process.isPendingFundAllocation()
		&& process.getRequest().hasAnyAccountingUnitFinancerWithNoFundsAllocated(getLoggedPerson());
    }

}
