package pt.ist.expenditureTrackingSystem.domain.organization;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.util.Address;

public class DeliveryInfo extends DeliveryInfo_Base {

    protected DeliveryInfo() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public DeliveryInfo(Person person, String recipient, Address address) {
	checkParameters(person,recipient,address);
	setPerson(person);
	setRecipient(recipient);
	setAddress(address);
    }

    private void checkParameters(Person person, String recipient, Address address) {
	if (person == null || recipient == null || address == null) {
	    throw new DomainException("deliveryInfo.message.exception.parametersCannotBeNull");
	}
    }

}
