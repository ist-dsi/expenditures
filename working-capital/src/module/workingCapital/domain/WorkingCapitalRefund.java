package module.workingCapital.domain;

import module.organization.domain.Person;
import module.workingCapital.domain.util.PaymentMethod;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;

public class WorkingCapitalRefund extends WorkingCapitalRefund_Base {
    
    public WorkingCapitalRefund() {
        super();
    }

    public WorkingCapitalRefund(final WorkingCapital workingCapital, final Person person, final Money value, final PaymentMethod paymentMethod) {
	this();
	setWorkingCapital(workingCapital);
	setPerson(person);
	setValue(value);
	setPaymentMethod(paymentMethod);
    }

    @Override
    public String getDescription() {
	final String prefix = BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "label.module.workingCapital.transaction.WorkingCapitalRefund");
	final PaymentMethod paymentMethod = getPaymentMethod();
	return paymentMethod == null ? prefix : prefix + ": " + paymentMethod.getLocalizedName();
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
