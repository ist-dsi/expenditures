package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.AbstractChangeFinancersAccountingUnit;

public class ChangeFinancersAccountingUnit extends AbstractChangeFinancersAccountingUnit<RegularAcquisitionProcess> {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	return process.isAccountingEmployeeForOnePossibleUnit() || process.isProjectAccountingEmployeeForOnePossibleUnit();
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return isCurrentUserProcessOwner(process) && process.getAcquisitionProcessState().isInAllocatedToSupplierState()
		&& process.getAcquisitionRequest().hasAnyAccountingUnitFinancerWithNoFundsAllocated(getLoggedPerson());
    }

}
