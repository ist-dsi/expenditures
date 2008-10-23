package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;

public class ChangeFinancersAccountingUnit extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	return process.isAccountingEmployee();
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return process.getAcquisitionProcessState().isInAllocatedToSupplierState()
		&& process.hasAllocatedFundsForAllProjectFinancers()
		&& !process.getAcquisitionRequest().getFinancersWithFundsAllocated(getUser().getPerson()).isEmpty();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {

    }

}
