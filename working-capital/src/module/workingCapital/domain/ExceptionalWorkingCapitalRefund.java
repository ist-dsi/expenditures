package module.workingCapital.domain;

import module.organization.domain.Person;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;

public class ExceptionalWorkingCapitalRefund extends ExceptionalWorkingCapitalRefund_Base {

    public ExceptionalWorkingCapitalRefund() {
	super();
    }

    public ExceptionalWorkingCapitalRefund(final WorkingCapital workingCapital, final Person person, final Money value,
	    final String caseDescription) {
	this();
	setWorkingCapital(workingCapital);
	setPerson(person);
	setValue(value);
	setAccumulatedValue(getPreviousTransaction().getAccumulatedValue());
	setBalance(getPreviousTransaction().getBalance().subtract(value));
	setDebt(getPreviousTransaction().getDebt().subtract(value));
	setCaseDescription(caseDescription);
    }

    @Override
    public String getDescription() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources",
		"label.module.workingCapital.transaction.ExceptionalWorkingCapitalRefund");
    }

    @Override
    public boolean isRefund() {
	return true;
    }

    @Override
    public boolean isExceptionalRefund() {
	return true;
    }

    public Money getRefundedValue() {
	return getValue();
    }

    public Person getOrigin() {
	final WorkingCapital workingCapital = getWorkingCapital();
	return workingCapital.getMovementResponsible();
    }
}
