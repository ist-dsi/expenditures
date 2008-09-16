package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.fenixWebFramework.security.UserView;

public class SubmitForApproval extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	User user = UserView.getUser();
	return user != null && user.getPerson().equals(process.getRequestor());
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return process.isProcessInState(AcquisitionProcessStateType.IN_GENESIS) && process.getAcquisitionRequest().isFilled()
		&& process.getAcquisitionRequest().isEveryItemFullyAttributedToPayingUnits();
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {
	new AcquisitionProcessState(process, AcquisitionProcessStateType.SUBMITTED_FOR_APPROVAL);
    }

    @Override
    protected void notifyUsers(AcquisitionProcess process) {
	notifyUnitsResponsibles(process);
    }
}
