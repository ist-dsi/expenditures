package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import java.util.List;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestWithPayment;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;

public class GenericAddPayingUnit<T extends PaymentProcess> extends AbstractActivity<T> {

    @Override
    protected boolean isAccessible(T process) {
	User user = getUser();
	return user != null && user.getPerson() == process.getRequestor();
    }

    @Override
    protected boolean isAvailable(T process) {
	return isCurrentUserProcessOwner(process) && process.isInGenesis();
    }

    @Override
    protected void process(T process, Object... objects) {
	List<Unit> units = (List<Unit>) objects[0];
	RequestWithPayment request = process.getRequest();
	for (Unit unit : units) {
	    request.addPayingUnit(unit);
	}
    }

}
