package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class UnSubmitForApproval extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && (loggedPerson.equals(process.getRequestor()) || process.isResponsibleForUnit(loggedPerson));
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return  super.isAvailable(process)
		&& process.getAcquisitionProcessState().isPendingApproval()
		&& !process.getAcquisitionRequest().isApprovedByAtLeastOneResponsible()
		//&& !process.getAcquisitionRequest().isAuthorizedByAtLeastOneResponsible()
		//&& !process.getAcquisitionRequest().isSubmittedForFundsAllocationByAllResponsibles()
		;
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	process.getAcquisitionRequest().unSubmitForFundsAllocation();
	process.inGenesis();
    }

}
