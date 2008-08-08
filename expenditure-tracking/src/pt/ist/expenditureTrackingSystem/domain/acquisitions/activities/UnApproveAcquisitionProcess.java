package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.security.UserView;

public class UnApproveAcquisitionProcess extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	return process.isResponsibleForUnit();
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	User user = UserView.getUser();
	return process.isApproved()
		|| (user != null && process.isProcessInState(AcquisitionProcessStateType.SUBMITTED_FOR_APPROVAL) && process
			.getAcquisitionRequest().hasBeenApprovedBy(user.getPerson()));
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {

	User user = UserView.getUser();
	Person person = user.getPerson();
	process.getAcquisitionRequest().unapproveBy(person);
	
	if (process.isApproved()) {
	    new AcquisitionProcessState(process, AcquisitionProcessStateType.SUBMITTED_FOR_APPROVAL);
	}
    }

}
