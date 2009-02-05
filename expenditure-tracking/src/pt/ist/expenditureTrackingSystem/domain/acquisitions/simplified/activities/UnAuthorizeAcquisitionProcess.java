package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class UnAuthorizeAcquisitionProcess extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null
		&& process.isResponsibleForUnit(loggedPerson, process.getAcquisitionRequest().getTotalItemValueWithAdditionalCostsAndVat());
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	final AcquisitionRequest acquisitionRequest = process.getRequest();
	return super.isAvailable(process)
		&& acquisitionRequest.hasBeenAuthorizedBy(loggedPerson)
		&& (process.getAcquisitionProcessState().isInAllocatedToUnitState() || process.isInAuthorizedState());
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	final Person loggedPerson = getLoggedPerson();
	process.getAcquisitionRequest().unathorizeBy(loggedPerson);
	process.allocateFundsToUnit();
    }

}
