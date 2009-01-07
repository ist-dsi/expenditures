package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.fenixWebFramework.security.UserView;

public abstract class GenericRefundProcessActivity extends AbstractActivity<RefundProcess>{

    public boolean isCurrentUserRequestor(RefundProcess process) {
	User user = UserView.getUser();
	return user != null && process.getRequest().getRequestor() == user.getPerson();
    }
    
    protected boolean isCurrentUserProcessOwner(RefundProcess process) {
	Person currentOwner = process.getCurrentOwner();
	User user = getUser();
	return currentOwner == null || (user != null && user.getPerson() == currentOwner);
    }
    
    @Override
    protected boolean isAvailable(RefundProcess process) {
	return isCurrentUserProcessOwner(process);
    }
}
