package org.fenixedu.bennu;

import org.fenixedu.bennu.spring.BennuSpringModule;
import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;

@BennuSpringModule(basePackages = "pt.ist.internalBilling.ui", bundles = "InternalBillingResources")
public class InternalBillingConfiguration {

    @ConfigurationManager(description = "Internal Billing Configuration")
    public interface ConfigurationProperties {
        @ConfigurationProperty(key = "print.app.token")
        public String printAppToken();
    }
    
    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

}
