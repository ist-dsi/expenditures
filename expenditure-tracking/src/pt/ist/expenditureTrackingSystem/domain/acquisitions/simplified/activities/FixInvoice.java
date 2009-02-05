package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class FixInvoice extends ReceiveInvoice {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	return userHasRole(RoleType.ACQUISITION_CENTRAL);
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	Person currentOwner = process.getCurrentOwner();
	final Person loggedPerson = getLoggedPerson();
	return process.getAcquisitionProcessState().isInvoiceReceived()
		&& (currentOwner == null || (loggedPerson != null && loggedPerson == currentOwner));
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	processInvoiceData(process, objects);
    }

}
