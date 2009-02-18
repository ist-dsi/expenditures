package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.security.UserView;

public class CancelInvoiceConfirmation extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(final RegularAcquisitionProcess process) {
	final User user = UserView.getUser();
	return user != null && process.isResponsibleForUnit(user.getPerson());
    }

    @Override
    protected boolean isAvailable(final RegularAcquisitionProcess process) {
	final User user = UserView.getUser();
	return  super.isAvailable(process)
		&& process.getAcquisitionRequest().isInvoiceConfirmedBy(user.getPerson())
		&& !process.hasAnyEffectiveFundAllocationId();
    }

    @Override
    protected void process(final RegularAcquisitionProcess process, final Object... objects) {
	Person person = (Person) objects[0];
	process.cancelInvoiceConfirmationBy(person);
    }

}
