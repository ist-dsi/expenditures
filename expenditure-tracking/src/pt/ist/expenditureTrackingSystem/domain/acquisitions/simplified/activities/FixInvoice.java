package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

public class FixInvoice extends ReceiveInvoice {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	return userHasRole(RoleType.ACQUISITION_CENTRAL);
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	User currentOwner = process.getCurrentOwner();
	return process.getAcquisitionProcessState().isInvoiceReceived()
		&& (currentOwner == null || process.isTakenByPerson(UserView.getCurrentUser()));
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	processInvoiceData(process, objects);
    }

}
