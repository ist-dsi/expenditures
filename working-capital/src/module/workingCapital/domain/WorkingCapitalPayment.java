package module.workingCapital.domain;

import module.organization.domain.Person;
import myorg.domain.util.Money;

public class WorkingCapitalPayment extends WorkingCapitalPayment_Base {

    public WorkingCapitalPayment() {
        super();
    }

    public WorkingCapitalPayment(final WorkingCapitalRequest workingCapitalRequest, final Person person, final String paymentIdentification) {
	this();
	setWorkingCapital(workingCapitalRequest.getWorkingCapital());
	setWorkingCapitalRequest(workingCapitalRequest);
	setPerson(person);
	final Money value = workingCapitalRequest.getRequestedValue();
	addDebt(value);
	setValue(value);
	setPaymentIdentification(paymentIdentification);
    }

    @Override
    public String getDescription() {
	final String paymentMethod = getWorkingCapitalRequest().getPaymentMethod().getLocalizedName();
	final String paymentIdentification = getPaymentIdentification();
	return paymentIdentification == null || paymentIdentification.isEmpty() ? paymentMethod : paymentMethod + ": " + paymentIdentification;
    }

    @Override
    public boolean isPayment() {
	return true;
    }

}

