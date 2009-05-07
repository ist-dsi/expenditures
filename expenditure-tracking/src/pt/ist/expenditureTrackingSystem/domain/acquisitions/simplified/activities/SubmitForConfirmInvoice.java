package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;

public class SubmitForConfirmInvoice extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	return userHasRole(RoleType.ACQUISITION_CENTRAL);
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return super.isAvailable(process) && process.isInvoiceReceived()
		&& process.getRequest().isCurrentTotalRealValueFullyDistributed();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	process.submitedForInvoiceConfirmation();
    }

}
