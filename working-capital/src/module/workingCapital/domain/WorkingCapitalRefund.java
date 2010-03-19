package module.workingCapital.domain;

import module.organization.domain.Person;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;

public class WorkingCapitalRefund extends WorkingCapitalRefund_Base {
    
    public WorkingCapitalRefund() {
        super();
    }

    public WorkingCapitalRefund(final WorkingCapital workingCapital, final Person person) {
	this();
	setWorkingCapital(workingCapital);
	setPerson(person);
    }

    @Override
    public String getDescription() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "label.module.workingCapital.transaction.WorkingCapitalRefund");
    }

    @Override
    public Money getAccumulatedValue() {
	return Money.ZERO;
    }

    @Override
    public Money getBalance() {
	return Money.ZERO;
    }

    @Override
    public Money getDebt() {
	return Money.ZERO;
    }

    @Override
    public Money getValue() {
	return Money.ZERO;
    }

    @Override
    public boolean isRefund() {
	return true;
    }

    public Person getOrigin() {
	final WorkingCapital workingCapital = getWorkingCapital();
	return workingCapital.getMovementResponsible();
    }

    public Money getRefundedValue() {
	return getPreviousTransaction().getBalance();
    }

}
