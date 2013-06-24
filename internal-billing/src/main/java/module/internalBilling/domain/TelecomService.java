package module.internalBilling.domain;

import pt.ist.bennu.core.domain.util.Money;

public class TelecomService extends TelecomService_Base {
    
    public TelecomService(final String identifier, final TelecomServiceType telecomServiceType) {
        super();
        setInternalBillingSystem(InternalBillingSystem.getInstance());
        setIdentifier(identifier);
        setTelecomServiceType(telecomServiceType);
        setMaxBillableValue(new Money("80"));
    }
    
}
