package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.fenixWebFramework.security.UserView;

public abstract class GenericRefundProcessActivity extends AbstractActivity<RefundProcess>{

    public boolean isCurrentUserRequestor(RefundProcess process) {
	User user = UserView.getUser();
	return user != null && process.getRequest().getRequestor() == user.getPerson();
    }
}
