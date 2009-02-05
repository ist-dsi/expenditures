package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class RevertInvoiceSubmission extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && process.isResponsibleForUnit(loggedPerson) || userHasRole(RoleType.ACQUISITION_CENTRAL);
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return super.isAvailable(process) && process.getAcquisitionProcessState().isPendingInvoiceConfirmation();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	process.invoiceReceived();
    }

}
