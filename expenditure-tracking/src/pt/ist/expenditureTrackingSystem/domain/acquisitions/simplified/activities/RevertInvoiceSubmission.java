package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.fenixWebFramework.security.UserView;

public class RevertInvoiceSubmission extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	User user = UserView.getUser();
	return user != null && process.isResponsibleForUnit(user.getPerson()) || userHasRole(RoleType.ACQUISITION_CENTRAL);
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return super.isAvailable(process) && process.getAcquisitionProcessState().isPendingInvoiceConfirmation();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	process.invoiceReceived();
    }

}
