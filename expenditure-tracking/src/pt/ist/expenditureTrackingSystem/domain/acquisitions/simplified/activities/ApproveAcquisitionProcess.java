package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class ApproveAcquisitionProcess extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	User user = getUser();
	return user != null && process.isResponsibleForUnit(user.getPerson())
		&& !process.getAcquisitionRequest().hasBeenApprovedBy(user.getPerson());
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return process.isProcessInState(AcquisitionProcessStateType.FUNDS_ALLOCATED);
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {
	Person person = (Person) objects[0];
	process.getAcquisitionRequest().approvedBy(person);
	if (process.getAcquisitionRequest().isApprovedByAllResponsibles()) {
	    new AcquisitionProcessState(process, AcquisitionProcessStateType.APPROVED);
	}
    }

}
