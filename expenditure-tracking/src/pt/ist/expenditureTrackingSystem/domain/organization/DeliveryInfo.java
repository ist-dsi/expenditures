package pt.ist.expenditureTrackingSystem.domain.organization;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.util.Address;

public class DeliveryInfo extends DeliveryInfo_Base {

    protected DeliveryInfo() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public DeliveryInfo(Person person, String recipient, Address address) {
	setPerson(person);
	setRecipient(recipient);
	setAddress(address);
    }

}
