package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class Refundee extends Refundee_Base {

    public Refundee(Person person) {
	super();
	setPerson(person);
    }

    public Refundee(String name, String fiscalCode) {
	super();
	setName(name);
	setFiscalCode(fiscalCode);
    }

    public boolean isInternalRefundee() {
	return hasPerson();
    }

    public String getRefundeePresentation() {
	Person person = getPerson();
	return person == null ? getName() + " (" + getFiscalCode() + ")" : person.getName() + " (" + person.getUsername() + ")";
    }

    public static Refundee getExternalRefundee(String name, String fiscalCode) {
	for (Refundee refundee : ExpenditureTrackingSystem.getInstance().getRefundees()) {
	    if (refundee.getName().equalsIgnoreCase(name) && refundee.getFiscalCode().equals(fiscalCode)) {
		return refundee;
	    }
	}
	return null;
    }

    @Override
    public String getName() {
	return hasPerson() ? getPerson().getName() : super.getName();
    }

    @Override
    public boolean isConnectedToCurrentHost() {
	return getExpenditureTrackingSystem() == ExpenditureTrackingSystem.getInstance();
    }

}
