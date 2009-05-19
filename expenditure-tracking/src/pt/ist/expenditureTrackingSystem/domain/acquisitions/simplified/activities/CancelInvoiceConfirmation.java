package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class CancelInvoiceConfirmation extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(final RegularAcquisitionProcess process) {
	final User user = UserView.getCurrentUser();
	return user != null && process.isResponsibleForUnit(user.getExpenditurePerson());
    }

    @Override
    protected boolean isAvailable(final RegularAcquisitionProcess process) {
	final User user = UserView.getCurrentUser();
	return super.isAvailable(process) && !process.getConfirmedInvoices(user.getExpenditurePerson()).isEmpty()
		&& !process.hasAnyEffectiveFundAllocationId();
    }

    @Override
    protected void process(final RegularAcquisitionProcess process, final Object... objects) {
	Person person = (Person) objects[0];
	process.cancelInvoiceConfirmationBy(person);
    }

}
