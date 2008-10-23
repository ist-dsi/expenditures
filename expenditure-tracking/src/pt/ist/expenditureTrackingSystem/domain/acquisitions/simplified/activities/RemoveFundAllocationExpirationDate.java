package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;

public class RemoveFundAllocationExpirationDate extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	return process.isAccountingEmployee();
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return (checkActiveConditions(process) || checkCanceledConditions(process))
		&& !process.getAcquisitionRequest().hasAnyFundAllocationId()
		&& !process.getAcquisitionRequest().hasAllocatedFundsForAnyProjectFinancers()
		&& process.getFundAllocationExpirationDate() != null;
    }

    private boolean checkActiveConditions(RegularAcquisitionProcess process) {
	return process.getAcquisitionProcessState().isInAllocatedToSupplierState();
    }

    private boolean checkCanceledConditions(RegularAcquisitionProcess process) {
	return process.getAcquisitionProcessState().isCanceled();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	process.setFundAllocationExpirationDate(null);
	process.getAcquisitionRequest().unSubmitForFundsAllocation();
	if (!process.getAcquisitionProcessState().isCanceled()) {
	    process.submitForApproval();
	}
    }

}
