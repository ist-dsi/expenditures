package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.security.UserView;

public class ConfirmInvoice extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	User user = UserView.getUser();
	return user != null && process.isResponsibleForUnit(user.getPerson())
		&& !process.getAcquisitionRequest().isInvoiceConfirmedBy(user.getPerson());
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return process.isProcessInState(AcquisitionProcessStateType.SUBMITTED_FOR_CONFIRM_INVOICE);
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {

	Person person = (Person) objects[0];
	process.getAcquisitionRequest().confirmInvoiceFor(person);
	if (process.getAcquisitionRequest().isInvoiceConfirmedBy()) {
	    new AcquisitionProcessState(process, AcquisitionProcessStateType.INVOICE_CONFIRMED);
	}
    }

}
