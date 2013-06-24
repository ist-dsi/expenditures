package module.internalBilling.domain;

import pt.ist.bennu.core.domain.exceptions.DomainException;

public class TelecomServiceType extends TelecomServiceType_Base {
    
    public TelecomServiceType(final String name, final String description) {
        super();
        setInternalBillingSystem(InternalBillingSystem.getInstance());
        setName(name);
        setDescription(description);
    }

    @Override
    public void setName(final String name) {
	checkIsUnique(name);
        super.setName(name);
    }

    private void checkIsUnique(final String name) {
	for (final TelecomServiceType telecomServiceType : InternalBillingSystem.getInstance().getTelecomServiceTypeSet()) {
	    if (telecomServiceType != this && telecomServiceType.getName().equalsIgnoreCase(name)) {
		throw new DomainException("error.duplicate.telecomServiceType", name);
	    }
	}
    }

}
