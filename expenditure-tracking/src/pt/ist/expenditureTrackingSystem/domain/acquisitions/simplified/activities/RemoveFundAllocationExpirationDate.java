package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;

public class RemoveFundAllocationExpirationDate extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	return process.isAccountingEmployee();
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return checkActiveConditions(process) || checkCanceledConditions(process);
    }

    private boolean checkActiveConditions(AcquisitionProcess process) {
	return process.getAcquisitionProcessState().isInState(AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER);
    }

    private boolean checkCanceledConditions(AcquisitionProcess process) {
	return process.getAcquisitionProcessState().isCanceled()
		&& !process.getAcquisitionRequest().hasAllFundAllocationId() && process.getFundAllocationExpirationDate() != null;
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {
	process.setFundAllocationExpirationDate(null);
	process.getAcquisitionRequest().unSubmitForFundsAllocation();
	if (!process.getAcquisitionProcessState().isCanceled()) {
	    new AcquisitionProcessState(process, AcquisitionProcessStateType.SUBMITTED_FOR_APPROVAL);
	}
    }

}
