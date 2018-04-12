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

        //SmartSigner
        @ConfigurationProperty(key = "smartsigner.integration", description = "Wether integration with SmartSigner is enabled")
        public boolean smartsignerIntegration();

        @ConfigurationProperty(key = "smartsigner.queue", description = "Name of the queue this instance will write to")
        public String queue();

        @ConfigurationProperty(key = "smartsigner.jwt.secret",
                description = "JWT secret shared between SmartSigner, Fenix and DOT")
        public String jwtSecret();

        @ConfigurationProperty(key = "smartsigner.signatureField",
                description = "Signature field present on the pdf document where the signature appearence will be stamped")
        public String signatureField();

        @ConfigurationProperty(key = "smartsigner.url", description = "SmartSigner URL")
        public String smartsignerUrl();

        @ConfigurationProperty(key = "smartsigner.addRequestEndpoint",
                description = "String to be concatenated to smartsigner.url to get the add request endpoint")
        public String addRequestEndpoint();

        @ConfigurationProperty(key = "smartsigner.checkSSLCertificates",
                description = "Check SmartSigner SSL certificates (deactivate for local debugging)")
        public boolean checkSSLCertificates();

        @ConfigurationProperty(key = "expenditures.jwtSecret", description = "Simmetric key not shared with any other system")
        public String expendituresJwtSecret();

        @ConfigurationProperty(key = "expenditures.senderEmail", description = "Email for sending purchase orders to suppliers")
        public String senderEmail();
    }

    public static ConfigurationProperties get() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }
}
