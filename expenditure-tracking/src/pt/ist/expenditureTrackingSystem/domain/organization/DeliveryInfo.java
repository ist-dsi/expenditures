package pt.ist.expenditureTrackingSystem.domain.organization;

import myorg.domain.util.Address;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

public class DeliveryInfo extends DeliveryInfo_Base {

    protected DeliveryInfo() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public DeliveryInfo(Person person, String recipient, Address address, String phone, String email) {
	checkParameters(person, recipient, address);
	setPerson(person);
	setRecipient(recipient);
	setAddress(address);
	setPhone(phone);
	setEmail(email);
    }

    private void checkParameters(Person person, String recipient, Address address) {
	if (person == null || recipient == null || address == null) {
	    throw new DomainException("deliveryInfo.message.exception.parametersCannotBeNull");
	}
    }

}
