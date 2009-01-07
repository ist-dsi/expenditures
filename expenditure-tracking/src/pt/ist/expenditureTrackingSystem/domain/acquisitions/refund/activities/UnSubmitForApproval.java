package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundRequest;
import pt.ist.fenixWebFramework.security.UserView;

public class UnSubmitForApproval extends GenericRefundProcessActivity {

    @Override
    protected boolean isAccessible(final RefundProcess process) {
	User user = UserView.getUser();
	return user != null && (user.getPerson() == process.getRequestor() || process.isResponsibleForUnit(user.getPerson()));
    }

    @Override
    protected boolean isAvailable(final RefundProcess process) {
	final RefundRequest refundRequest = process.getRequest();
	return  super.isAvailable(process)
		&& process.getProcessState().isPendingApproval()
		&& !refundRequest.isApprovedByAtLeastOneResponsible();
    }

    @Override
    protected void process(final RefundProcess process, final Object... objects) {
	process.unSubmitForApproval();
    }

}
