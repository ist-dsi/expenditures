package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.ProcessComment;

public class RejectAcquisitionProcess extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && process.isResponsibleForUnit(loggedPerson)
		&& !process.getAcquisitionRequest().hasBeenAuthorizedBy(loggedPerson);
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return  super.isAvailable(process) && process.isPendingApproval();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	new ProcessComment(process, getLoggedPerson(), (String)objects[0]);
	process.reject();
    }

}
