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

        @ConfigurationProperty(key = "smartsigner.queue.expenditure.simplified.purchase.order",
                description = "Name of the queue for simplified procedure purchase orders")
        public String queueSimplifiedPurchaseOrder();

        @ConfigurationProperty(key = "senderEmail.simplified.purchase.order",
                description = "Email Sender for sending purchase orders to suppliers")
        public String senderEmailSimplifiedPurchaseOrder();

        @ConfigurationProperty(key = "smartsigner.queue.expenditure.consultation.management.document",
                description = "Name of the queue for consultation managment documents")
        public String queueConsultationManagementDocument();

        @ConfigurationProperty(key = "smartsigner.queue.expenditure.consultation.purchase.order",
                description = "Name of the queue for consultation purchase orders")
        public String queueConsultationPurchaseOrder();

        @ConfigurationProperty(key = "papyrus.integration.enabled", description = "Wether integration with papyrus is enabled")
        public boolean isPapyrusIntegrationEnabled();

        @ConfigurationProperty(key = "papyrus.url", description = "URL for the Papyrus back-end")
        public String papyrusUrl();

        @ConfigurationProperty(key = "papyrus.token", description = "Token to acess the Papyrus back-end")
        public String papyrusToken();

        @ConfigurationProperty(key = "papyrus.template.purchaseOrderDocument",
                description = "Name of the template of the purchase order document to be sent to papyrus for pdf generation")
        public String papyrusTemplateForPurchaseOrderDocument();

        @ConfigurationProperty(key = "papyrus.template.purchaseOrderDocument.signatureField.name",
                description = "Name of signature form field to be sent to SmartSigner")
        public String papyrusTemplatePurchaseOrderDocumentSignatureFieldName();

        @ConfigurationProperty(key = "papyrus.template.purchaseOrderDocument.signatureField.leftx",
                description = "Coordinate x, measured from left of page, for the signature field on Purchase Order Document")
        public float papyrusTemplatePurchaseOrderDocumentSignatureFieldLeftx();

        @ConfigurationProperty(key = "papyrus.template.purchaseOrderDocument.signatureField.downy",
                description = "Coordinate y, measured from bottom of page, for the signature field on Purchase Order Document")
        public float papyrusTemplatePurchaseOrderDocumentSignatureFieldDowny();

        @ConfigurationProperty(key = "papyrus.template.purchaseOrderDocument.signatureField.width",
                description = "Width for the signature field on Purchase Order Document")
        public float papyrusTemplatePurchaseOrderDocumentSignatureFieldWidth();

        @ConfigurationProperty(key = "papyrus.template.purchaseOrderDocument.signatureField.height",
                description = "Height for the signature field on Purchase Order Document")
        public float papyrusTemplatePurchaseOrderDocumentSignatureFieldHeight();
    }

    public static ConfigurationProperties get() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }
}
