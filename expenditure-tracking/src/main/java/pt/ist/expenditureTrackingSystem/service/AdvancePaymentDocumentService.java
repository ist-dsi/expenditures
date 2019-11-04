package pt.ist.expenditureTrackingSystem.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.fenixedu.commons.i18n.I18N;

import com.google.common.io.ByteStreams;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem._development.ExpenditureConfiguration;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AdvancePaymentRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;
import pt.ist.papyrus.PapyrusClient;
import pt.ist.papyrus.PapyrusSettings;

public class AdvancePaymentDocumentService {

    public static byte[] produceAdvancePaymentDocument(SimplifiedProcedureProcess process) {
        try {
            final PapyrusClient papyrusClient =
                    new PapyrusClient(ExpenditureConfiguration.get().papyrusUrl(), ExpenditureConfiguration.get().papyrusToken());

            final InputStream templateAsStream =
                    PurchaseOrderDocumentService.class.getResourceAsStream("/templates/" + I18N.getLocale().getLanguage() + "/"
                            + ExpenditureConfiguration.get().papyrusTemplateForAdvancePaymentDocument());

            final JsonObject ctx = getAdvancePaymentDocumentJson(process);
            InputStream document = papyrusClient.liveRender(templateAsStream, ctx,
                    PapyrusSettings.newBuilder().landscape(false).format("A4").build());

            document = CreateDocumentServiceUtils.addSignatureFieldOnLastPage(document,
                    ExpenditureConfiguration.get().papyrusTemplatePurchaseOrderDocumentSignatureFieldName(),
                    ExpenditureConfiguration.get().papyrusTemplatePurchaseOrderDocumentSignatureFieldLeftx(),
                    ExpenditureConfiguration.get().papyrusTemplatePurchaseOrderDocumentSignatureFieldDowny(),
                    ExpenditureConfiguration.get().papyrusTemplatePurchaseOrderDocumentSignatureFieldWidth(),
                    ExpenditureConfiguration.get().papyrusTemplatePurchaseOrderDocumentSignatureFieldHeight());

            return ByteStreams.toByteArray(document);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new DomainException(Bundle.ACQUISITION, "acquisitionRequestDocument.message.exception.failedCreation");
        }
    }

    private static JsonObject getAdvancePaymentDocumentJson(SimplifiedProcedureProcess process) {
        final JsonObject purchaseOrderDocumentJson = new JsonObject();

        final JsonObject acquisition = new JsonObject();
        AcquisitionRequest acquisitionRequest = process.getAcquisitionRequest();
        AdvancePaymentRequest advancePaymentRequest = acquisitionRequest.getAdvancePaymentRequest();

        purchaseOrderDocumentJson.addProperty("documentRecipient",
                ExpenditureConfiguration.get().advancePaymentDocumentRecipient());

        purchaseOrderDocumentJson.addProperty("advancePaymentPurpose",
                advancePaymentRequest.getAdvancePaymentDocumentTemplate().getSubject().getContent());
        purchaseOrderDocumentJson.addProperty("advancePaymentLegalBase",
                advancePaymentRequest.getAdvancePaymentDocumentTemplate().getType().getContent());
        purchaseOrderDocumentJson.addProperty("advancePaymentPaymentMethod",
                advancePaymentRequest.getPaymentMethod().getLocalizedName());
        if (advancePaymentRequest.getPercentage() != null) {
            purchaseOrderDocumentJson.addProperty("advancePaymentPercentage", advancePaymentRequest.getPercentage());
        }

        if (advancePaymentRequest.getAcquisitionJustification() != null) {
            purchaseOrderDocumentJson.addProperty("acquisitionJustification",
                    advancePaymentRequest.getAcquisitionJustification());
        }

        if (advancePaymentRequest.getEntityJustification() != null) {
            purchaseOrderDocumentJson.addProperty("entityJustification", advancePaymentRequest.getEntityJustification());
        }

        if (acquisitionRequest.getSupplier() != null) {
            final JsonObject supplier = new JsonObject();
            supplier.addProperty("name", acquisitionRequest.getSupplier().getName());
            supplier.addProperty("fiscalIdentificationCode", acquisitionRequest.getSupplier().getFiscalIdentificationCode());
            acquisition.add("supplier", supplier);
        }

        acquisition.addProperty("acquisitionProcessId", acquisitionRequest.getAcquisitionProcessId());
        acquisition.addProperty("totalItemValue", acquisitionRequest.getTotalItemValue().toFormatString());
        acquisition.addProperty("totalItemValueWithVat", acquisitionRequest.getTotalItemValueWithVat().toFormatString());
        acquisition.addProperty("totalVatValue", acquisitionRequest.getTotalVatValue().toFormatString());
        acquisition.addProperty("contractSimpleDescription", acquisitionRequest.getContractSimpleDescription());
        purchaseOrderDocumentJson.add("acquisitionRequest", acquisition);

        final JsonArray acquisitionRequestItems = new JsonArray();
        SortedSet<AcquisitionRequestItem> orderedRequestItemsSet = acquisitionRequest.getOrderedRequestItemsSet();
        for (int index = 1; index <= orderedRequestItemsSet.size(); index++) {
            final AcquisitionRequestItem acquisitionRequestItem = orderedRequestItemsSet.iterator().next();
            final JsonObject acquisitionRequestItemJson = new JsonObject();

            acquisitionRequestItemJson.addProperty("itemNumber", index);
            acquisitionRequestItemJson.addProperty("description", acquisitionRequestItem.getDescription());
            acquisitionRequestItemJson.addProperty("quantity", acquisitionRequestItem.getQuantity());
            acquisitionRequestItemJson.addProperty("unitValue",
                    acquisitionRequestItem.getUnitValue().toFormatStringWithoutCurrency());
            acquisitionRequestItemJson.addProperty("vatValue", acquisitionRequestItem.getVatValue().toPlainString());
            acquisitionRequestItemJson.addProperty("totalItemValueWithVat",
                    acquisitionRequestItem.getTotalItemValueWithVat().toFormatStringWithoutCurrency());
            acquisitionRequestItems.add(acquisitionRequestItemJson);
        }
        purchaseOrderDocumentJson.add("acquisitionRequestItems", acquisitionRequestItems);

        final JsonArray responsibles = new JsonArray();

        Set<Person> responsibleSet = process.getAcquisitionApprovalTermSet().stream().map(aap -> aap.getApprover()).distinct()
                .collect(Collectors.toSet());

        for (Person person : responsibleSet) {
            final JsonObject responsibleJson = new JsonObject();
            responsibleJson.addProperty("responsibleName", person.getUser().getDisplayName());
            responsibleJson.addProperty("responsibleUsername", person.getUsername());
            responsibles.add(responsibleJson);
        }
        purchaseOrderDocumentJson.add("responsibles", responsibles);

        acquisitionRequest.getProjectFinancersWithFundsAllocated();
        final JsonArray financersWithFundsAllocated = new JsonArray();
        for (final Financer financer : acquisitionRequest.getFinancersWithFundsAllocated()) {
            final JsonObject financerJson = new JsonObject();
            if (financer instanceof ProjectFinancer) {
                financerJson.addProperty("shortIdentifier", financer.getUnit().getShortIdentifier());
                financerJson.addProperty("name", financer.getUnit().getName());
            } else {
                financerJson.addProperty("shortIdentifier", financer.getFinancerCostCenter().getShortIdentifier());
                financerJson.addProperty("name", financer.getFinancerCostCenter().getName());
            }
            financersWithFundsAllocated.add(financerJson);
        }
        purchaseOrderDocumentJson.add("financersWithFundsAllocated", financersWithFundsAllocated);

        purchaseOrderDocumentJson.addProperty("currentDate", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));

        UUID uuid = UUID.randomUUID();
        purchaseOrderDocumentJson.addProperty("qrcodeImage", CreateDocumentServiceUtils.generateURIBase64QRCode(uuid.toString()));
        purchaseOrderDocumentJson.addProperty("uuid", uuid.toString());

        final InputStream resourceAsStream = PurchaseOrderDocumentService.class.getResourceAsStream("/images/Logo.png");
        try {
            byte[] byteArray = IOUtils.toByteArray(resourceAsStream);
            purchaseOrderDocumentJson.addProperty("imageBase64", Base64.getEncoder().encodeToString(byteArray));
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error(e);
        }
        return purchaseOrderDocumentJson;
    }
}
