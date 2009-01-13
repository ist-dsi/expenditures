package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.fenixWebFramework.security.UserView;

public abstract class PaymentProcess extends PaymentProcess_Base {

    public PaymentProcess() {
	super();
    }

    public abstract <T extends RequestWithPayment> T getRequest();

    public List<Unit> getPayingUnits() {
	List<Unit> res = new ArrayList<Unit>();
	for (Financer financer : getRequest().getFinancers()) {
	    res.add(financer.getUnit());
	}
	return res;
    }

    public boolean isRealValueEqualOrLessThanFundAllocation() {
	Money allocatedMoney = this.getRequest().getTotalValue();
	Money realMoney = this.getRequest().getRealTotalValue();
	return realMoney.isLessThanOrEqual(allocatedMoney);
    }

    public boolean isResponsibleForAtLeastOnePayingUnit(Person person) {
	for (Unit unit : getPayingUnits()) {
	    if (unit.isResponsible(person)) {
		return true;
	    }
	}
	return false;
    }

    public Person getRequestor() {
	return getRequest().getRequester();
    }

    public boolean isResponsibleForUnit(Person person) {
	Set<Authorization> validAuthorizations = person.getValidAuthorizations();
	for (Unit unit : getPayingUnits()) {
	    for (Authorization authorization : validAuthorizations) {
		if (unit.isSubUnit(authorization.getUnit())) {
		    return true;
		}
	    }
	}

	return false;
    }

    public boolean isResponsibleForUnit() {
	User user = UserView.getUser();
	if (user == null) {
	    return false;
	}
	return isResponsibleForUnit(user.getPerson());
    }

    public boolean isProjectAccountingEmployee(final Person person) {
	return getRequest().isProjectAccountingEmployee(person);
    }

    public boolean isProjectAccountingEmployee() {
	final User user = UserView.getUser();
	return user != null && isProjectAccountingEmployee(user.getPerson());
    }

    public boolean hasAllocatedFundsForAllProjectFinancers(final Person person) {
	return getRequest().hasAllocatedFundsForAllProjectFinancers(person);
    }

    public boolean isAccountingEmployee(final Person person) {
	return getRequest().isAccountingEmployee(person);
    }

    public boolean isAccountingEmployee() {
	final User user = UserView.getUser();
	return user != null && isAccountingEmployee(user.getPerson());
    }

    public boolean isAccountingEmployeeForOnePossibleUnit() {
	final User user = UserView.getUser();
	return user != null && isAccountingEmployeeForOnePossibleUnit(user.getPerson());
    }

    public boolean isAccountingEmployeeForOnePossibleUnit(final Person person) {
	return getRequest().isAccountingEmployeeForOnePossibleUnit(person);
    }

    public boolean hasAllocatedFundsForAllProjectFinancers() {
	return getRequest().hasAllocatedFundsForAllProjectFinancers();
    }

    public boolean hasAnyAllocatedFunds() {
	return getRequest().hasAnyAllocatedFunds();
    }

    public boolean hasAllFundAllocationId(Person person) {
	return getRequest().hasAllFundAllocationId(person);
    }

    public abstract boolean isInGenesis();

    public abstract boolean isPendingApproval();

}
