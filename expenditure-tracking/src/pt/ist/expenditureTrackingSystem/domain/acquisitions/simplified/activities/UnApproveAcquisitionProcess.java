package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class UnApproveAcquisitionProcess extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && process.isResponsibleForUnit(loggedPerson);
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return super.isAvailable(process) && process.getFundAllocationExpirationDate() == null
		&& process.getAcquisitionRequest().hasBeenApprovedBy(loggedPerson);
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	final Person loggedPerson = getLoggedPerson();
	process.getAcquisitionRequest().unSubmitForFundsAllocation(loggedPerson);
	process.submitForApproval();
    }

}
