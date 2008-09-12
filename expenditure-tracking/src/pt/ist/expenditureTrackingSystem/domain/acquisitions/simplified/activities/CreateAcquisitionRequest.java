package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;

public class CreateAcquisitionRequest extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	return userHasRole(RoleType.ACQUISITION_CENTRAL);
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return process.isProcessInState(AcquisitionProcessStateType.APPROVED);
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {
	if (objects.length == 2 && (objects[0] instanceof byte[])) {
	    new AcquisitionRequestDocument(process.getAcquisitionRequest(), (byte[]) objects[0], (String) objects[1]);
	} else {
	    new AcquisitionRequestDocument(process.getAcquisitionRequest());
	}
    }

}
