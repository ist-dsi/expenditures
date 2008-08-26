package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;

public class RemoveFundAllocation extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	return userHasRole(RoleType.ACCOUNTABILITY);
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return checkActiveConditions(process) || checkCanceledConditions(process);
    }

    private boolean checkActiveConditions(AcquisitionProcess process) {
	return process.isProcessInState(AcquisitionProcessStateType.FUNDS_ALLOCATED);
    }

    private boolean checkCanceledConditions(AcquisitionProcess process) {
	return process.isProcessInState(AcquisitionProcessStateType.CANCELED)
		&& process.getFundAllocationExpirationDate() == null
		&& process.getFundAllocationId() != null;
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {
	process.setFundAllocationId(null);
	if (!process.isProcessInState(AcquisitionProcessStateType.CANCELED)) {
	    new AcquisitionProcessState(process, AcquisitionProcessStateType.APPROVED);
	}
    }

}
