package module.workingCapital.domain;

import module.organization.domain.Accountability;
import module.organization.domain.Person;
import module.workingCapital.domain.util.PaymentMethod;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.domain.util.Money;

import org.joda.time.DateTime;

public class WorkingCapitalRequest extends WorkingCapitalRequest_Base {

    public WorkingCapitalRequest() {
        super();
        final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstance();
        setWorkingCapitalSystem(workingCapitalSystem);
        setRequestCreation(new DateTime());
        final User user = UserView.getCurrentUser();
        final Accountability accountability = workingCapitalSystem.getAccountingAccountability(user);
        setWorkingCapitalRequester(accountability);
    }

    public WorkingCapitalRequest(final WorkingCapital workingCapital, final Money requestedValue, final PaymentMethod paymentMethod) {
	this();
	setWorkingCapital(workingCapital);
	setRequestedValue(requestedValue);
	setPaymentMethod(paymentMethod);
    }

    public boolean isRequestProcessedByTreasury() {
	return getProcessedByTreasury() != null;
    }

    public void pay(final User user) {
	setProcessedByTreasury(new DateTime());
	final Person person = user.getPerson();
	setWorkingCapitalTreasuryProcessor(person);
	new WorkingCapitalPayment(this, person);
    }

}
