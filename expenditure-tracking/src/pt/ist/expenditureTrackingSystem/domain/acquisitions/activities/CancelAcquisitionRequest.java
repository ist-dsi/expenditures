package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;

public class CancelAcquisitionRequest extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	return userHasRole(RoleType.ACQUISITION_CENTRAL);
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return process.isProcessInState(AcquisitionProcessStateType.ACQUISITION_PROCESSED);
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {
	new AcquisitionProcessState(process, AcquisitionProcessStateType.CANCELED);
    }

}
