package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class CancelAcquisitionRequest extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {

	return userHasRole(RoleType.ACQUISITION_CENTRAL) || isUserOwnerOfProcess(process)
		|| isUserResponsibleForAuthorizingPayment(process) || isUserResponsibleForUnit(process);
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	AcquisitionProcessState acquisitionProcessState = process.getAcquisitionProcessState();
	return super.isAvailable(process)
		&& ((acquisitionProcessState.isAcquisitionProcessed() && userHasRole(RoleType.ACQUISITION_CENTRAL))
			|| (acquisitionProcessState.isInGenesis() && isUserOwnerOfProcess(process))
			|| (acquisitionProcessState.isInAllocatedToUnitState() && isUserResponsibleForAuthorizingPayment(process))
			|| (acquisitionProcessState.isPendingInvoiceConfirmation() && isUserResponsibleForUnit(process)) || (acquisitionProcessState
			.isInvoiceReceived() && userHasRole(RoleType.ACQUISITION_CENTRAL)));
    }

    private boolean isUserResponsibleForAuthorizingPayment(RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null
		&& process.isResponsibleForUnit(loggedPerson, process.getAcquisitionRequest()
			.getTotalItemValueWithAdditionalCostsAndVat())
		&& !process.getAcquisitionRequest().hasBeenAuthorizedBy(loggedPerson);
    }

    private boolean isUserResponsibleForUnit(RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && process.isResponsibleForUnit(loggedPerson);
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	process.cancel();
    }

}
