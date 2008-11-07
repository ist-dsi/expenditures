package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;

public class PayAcquisition extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	return userHasRole(RoleType.TREASURY);
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return  super.isAvailable(process) && process.getAcquisitionProcessState().isAllocatedPermanently();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	process.getAcquisitionRequest().setPaymentReference((String)objects[0]);
	process.acquisitionPayed();
    }

}
