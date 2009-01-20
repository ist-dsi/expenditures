package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.fenixWebFramework.security.UserView;

public class RefundPerson extends GenericRefundProcessActivity {

    @Override
    protected boolean isAccessible(final RefundProcess process) {
	final User user = UserView.getUser();
	return user != null && user.getPerson().hasRoleType(RoleType.TREASURY);
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
