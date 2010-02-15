package module.workingCapital.domain;

import module.organization.domain.Person;
import myorg.domain.util.Money;

public class WorkingCapitalPayment extends WorkingCapitalPayment_Base {

    public WorkingCapitalPayment() {
        super();
    }

    public WorkingCapitalPayment(final WorkingCapitalRequest workingCapitalRequest, final Person person) {
	this();
	setWorkingCapital(workingCapitalRequest.getWorkingCapital());
	setWorkingCapitalRequest(workingCapitalRequest);
	setPerson(person);
	final Money value = workingCapitalRequest.getRequestedValue();
	addDebt(value);
    }

    @Override
    public String getDescription() {
	return getWorkingCapitalRequest().getPaymentMethod().getLocalizedName();
    }

}

