package module.internalBilling.domain;

import pt.ist.bennu.core.domain.VirtualHost;

public class InternalBillingSystem extends InternalBillingSystem_Base {
    
    private InternalBillingSystem(final VirtualHost virtualHost) {
        super();
        setVirtualHost(virtualHost);
    }

    public static InternalBillingSystem getInstance() {
	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	if (virtualHost != null) {
	    return virtualHost.getInternalBillingSystem() == null ?
		new InternalBillingSystem(virtualHost) : virtualHost.getInternalBillingSystem();
	}
	return null;
    }

}
