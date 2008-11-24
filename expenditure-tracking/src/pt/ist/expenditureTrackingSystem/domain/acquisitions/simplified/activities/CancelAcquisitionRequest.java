package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;

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
			|| (acquisitionProcessState.isInAllocatedToUnitState() && isUserResponsibleForAuthorizingPayment(process)) || (acquisitionProcessState
			.isPendingInvoiceConfirmation() && isUserResponsibleForUnit(process)));
    }

    private boolean isUserResponsibleForAuthorizingPayment(RegularAcquisitionProcess process) {
	User user = getUser();
	return user != null
		&& process.isResponsibleForUnit(user.getPerson(), process.getAcquisitionRequest()
			.getTotalItemValueWithAdditionalCostsAndVat())
		&& !process.getAcquisitionRequest().hasBeenApprovedBy(user.getPerson());
    }

    private boolean isUserResponsibleForUnit(RegularAcquisitionProcess process) {
	User user = getUser();
	return user != null && process.isResponsibleForUnit(user.getPerson());
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	process.cancel();
    }

}
