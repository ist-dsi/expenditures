package pt.ist.expenditureTrackingSystem._development;

import myorg.domain.VirtualHost;

public class ExternalIntegration {

    public static boolean isActive() {
	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	return virtualHost != null && isActive(virtualHost.getHostname());
    }

    private static boolean isActive(final String hostname) {
	return false
		//"dot.ist-id.ist.utl.pt".equals(hostname)
		//|| "dot.ist.utl.pt".equals(hostname)
	;
    }

}
