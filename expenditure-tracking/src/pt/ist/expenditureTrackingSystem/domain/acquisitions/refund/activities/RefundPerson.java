package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class RefundPerson extends GenericRefundProcessActivity {

    @Override
    protected boolean isAccessible(final RefundProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && loggedPerson.hasRoleType(RoleType.TREASURY);
    }

    @Override
    protected boolean isAvailable(final RefundProcess process) {
	return super.isAvailable(process) && process.hasFundsAllocatedPermanently() && !process.isPayed();
    }

    @Override
    protected void process(final RefundProcess process, final Object... objects) {
	final String paymentReference = (String) objects[0];
	process.refundPerson(paymentReference);
    }

}
