package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.ProcessComment;

public class RejectAcquisitionProcess extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	User user = getUser();
	return user != null && process.isResponsibleForUnit(user.getPerson())
		&& !process.getAcquisitionRequest().hasBeenApprovedBy(user.getPerson());
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return process.isPendingApproval();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	User user = getUser();
	new ProcessComment(process, user.getPerson(), (String)objects[0]);
	process.reject();
    }

}
