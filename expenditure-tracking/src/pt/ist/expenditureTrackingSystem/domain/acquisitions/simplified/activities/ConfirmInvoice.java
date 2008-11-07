package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.security.UserView;

public class ConfirmInvoice extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	User user = UserView.getUser();
	return user != null && process.isResponsibleForUnit(user.getPerson())
		&& !process.getAcquisitionRequest().isInvoiceConfirmedBy(user.getPerson());
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return  super.isAvailable(process) && process.getAcquisitionProcessState().isPendingInvoiceConfirmation();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	Person person = (Person) objects[0];
	process.confirmInvoiceBy(person);
    }

}
