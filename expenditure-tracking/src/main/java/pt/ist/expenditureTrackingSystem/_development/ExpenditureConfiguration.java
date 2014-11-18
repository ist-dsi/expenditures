package pt.ist.expenditureTrackingSystem._development;

import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;

public class ExpenditureConfiguration {

    @ConfigurationManager(description = "Expenditure Module Configurations")
    public interface ConfigurationProperties {
        @ConfigurationProperty(key = "expenditures.api.token", description = "Expenditures API access token")
        public String apiToken();

        @ConfigurationProperty(key = "ssn", description = "Institutions SSN number")
        public String ssn();
        @ConfigurationProperty(key = "cae", description = "Institutions CAE number")
        public String cae();

        @ConfigurationProperty(key = "exportStructureService.username", description = "Export Structure Service Username")
        public String exportStructureServiceUsername();
        @ConfigurationProperty(key = "exportStructureService.password", description = "Export Structure Service Password")
        public String exportStructureServicePassword();

        @ConfigurationProperty(key = "user.photo.url", description = "User photo URL prefix")
        public String userPhotoUrl();

    }

    public static ConfigurationProperties get() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

}
