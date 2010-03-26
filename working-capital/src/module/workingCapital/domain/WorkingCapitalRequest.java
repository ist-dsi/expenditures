package module.workingCapital.domain;

import module.organization.domain.Accountability;
import module.organization.domain.Person;
import module.workingCapital.domain.util.PaymentMethod;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.domain.exceptions.DomainException;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;

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

    @Override
    public void setRequestedValue(final Money requestedValue) {
	final WorkingCapital workingCapital = getWorkingCapital();
	if (workingCapital == null) {
	    throw new NullPointerException();
	}
	if (!workingCapital.canRequestValue(requestedValue)) {
	    throw new DomainException(BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "error.insufficient.authorized.funds"));
	}
        super.setRequestedValue(requestedValue);
    }

}
