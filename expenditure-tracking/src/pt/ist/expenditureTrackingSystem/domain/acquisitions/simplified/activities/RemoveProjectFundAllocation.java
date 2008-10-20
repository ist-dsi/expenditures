package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;

public class RemoveProjectFundAllocation extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	return process.isProjectAccountingEmployee();
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return checkActiveConditions(process) || checkCanceledConditions(process);
    }

    private boolean checkActiveConditions(AcquisitionProcess process) {
	return process.getAcquisitionProcessState().isInState(AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER)
		&& process.hasAllocatedFundsForAllProjectFinancers();
    }

    private boolean checkCanceledConditions(AcquisitionProcess process) {
	return process.getAcquisitionProcessState().isCanceled()
		&& process.getAcquisitionRequest().hasAllocatedFundsForAllProjectFinancers();
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {
	process.getAcquisitionRequest().resetProjectFundAllocationId();
	if (process.getAcquisitionProcessState().isCanceled()
		&& !process.getAcquisitionRequest().hasAllFundAllocationId()) {
	    new RemoveFundAllocationExpirationDate().execute(process);
	}
    }
}
