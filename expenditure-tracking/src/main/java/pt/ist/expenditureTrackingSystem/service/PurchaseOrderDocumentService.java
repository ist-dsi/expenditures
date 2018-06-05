package pt.ist.expenditureTrackingSystem.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.commons.i18n.I18N;

import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfRectangle;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;

import module.finance.domain.SupplierContact;
import module.finance.util.Address;
import pt.ist.expenditureTrackingSystem._development.ExpenditureConfiguration;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.CreateAcquisitionPurchaseOrderDocument.AcquisitionRequestItemBean;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.CreateAcquisitionPurchaseOrderDocument.DeliveryLocal;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.CreateAcquisitionPurchaseOrderDocument.DeliveryLocalList;
import pt.ist.expenditureTrackingSystem.ui.QRCodeGenerator;
import pt.ist.papyrus.PapyrusClient;
import pt.ist.papyrus.PapyrusSettings;

/**
 * 
 * @author Ricardo Almeida
 *
 */
public class PurchaseOrderDocumentService {

    /**
     * Produces the Purchase Order Document with all relevant information, able to be signed
     * 
     * @param acquisitionRequest
     * @param requestID
     * @param supplierContact
     * @param deliveryLocalList
     * @param acquisitionRequestItemBeans
     * @param uuid
     * @return
     * @throws IOException
     */
    public static byte[] producePurchaseOrderDocument(final AcquisitionRequest acquisitionRequest, final String requestID,
            final SupplierContact supplierContact, DeliveryLocalList deliveryLocalList,
            List<AcquisitionRequestItemBean> acquisitionRequestItemBeans, UUID uuid) throws IOException {
        final PapyrusClient papyrusClient =
                new PapyrusClient(ExpenditureConfiguration.get().papyrusUrl(), ExpenditureConfiguration.get().papyrusToken());

        final InputStream templateAsStream =
                PurchaseOrderDocumentService.class.getResourceAsStream("/templates/" + I18N.getLocale().getLanguage() + "/"
                        + ExpenditureConfiguration.get().papyrusTemplateForPurchaseOrderDocument());

        final JsonObject ctx = getPurchaseOrderDocumentJson(acquisitionRequest, requestID, supplierContact, deliveryLocalList,
                acquisitionRequestItemBeans, uuid);

        InputStream document =
                papyrusClient.liveRender(templateAsStream, ctx, PapyrusSettings.newBuilder().landscape(true).size("A4").build());

        document = addSignatureFieldOnLastPage(document,
                ExpenditureConfiguration.get().papyrusTemplatePurchaseOrderDocumentSignatureFieldName(),
                ExpenditureConfiguration.get().papyrusTemplatePurchaseOrderDocumentSignatureFieldLeftx(),
                ExpenditureConfiguration.get().papyrusTemplatePurchaseOrderDocumentSignatureFieldDowny(),
                ExpenditureConfiguration.get().papyrusTemplatePurchaseOrderDocumentSignatureFieldWidth(),
                ExpenditureConfiguration.get().papyrusTemplatePurchaseOrderDocumentSignatureFieldHeight());

        return ByteStreams.toByteArray(document);
    }

    /**
     * Adds a signature form field to the last page of a PDF document
     * 
     * @param pdfDocumentBytes
     * @param signatureField
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    private static InputStream addSignatureFieldOnLastPage(InputStream pdfDocumentBytes, String signatureField, float x, float y,
            float width, float height) throws IOException {
        try {
            final File file = File.createTempFile("pdf", "addsignaturefield");
            final FileOutputStream fos = new FileOutputStream(file);
            final PdfReader reader = new PdfReader(pdfDocumentBytes);
            final PdfStamper pdfStamper = new PdfStamper(reader, fos);
            final int pageNumber = reader.getNumberOfPages();
            final PdfFormField signatureFormField =
                    getSignatureField(pdfStamper.getWriter(), pageNumber, signatureField, x, y, width, height);
            pdfStamper.addAnnotation(signatureFormField, pageNumber);
            pdfStamper.close();
            final InputStream is = new FileInputStream(file);
            file.delete();
            return is;
        } catch (final IOException | DocumentException e) {
            throw new IOException();
        }
    }

    private static PdfFormField getSignatureField(PdfWriter writer, int pageNumber, String fieldName, float x, float y,
            float width, float height) {
        final PdfFormField signature = PdfFormField.createSignature(writer);
        signature.put(PdfName.FT, PdfName.SIG);
        signature.put(PdfName.TYPE, PdfName.ANNOT);
        signature.put(PdfName.SUBTYPE, PdfName.WIDGET);
        signature.put(PdfName.P, writer.getPageReference(pageNumber));
        signature.put(PdfName.RECT, new PdfRectangle(x, y, x + width, y + height));
        signature.put(PdfName.T, new PdfString(fieldName, PdfObject.TEXT_UNICODE));
        signature.put(PdfName.F, new PdfNumber(4));
        //signature.setMKBackgroundColor(BaseColor.WHITE);
        final int offset = 15;
        signature.setWidget(new Rectangle(x, y - offset, x + width, y - offset + height), PdfAnnotation.HIGHLIGHT_INVERT);
        return signature;
    }

    private static String generateURIBase64QRCode(String uuid) {
        String encodedImage = "data:image/png;base64,";
        encodedImage += Base64.getEncoder().encodeToString((QRCodeGenerator.generate(uuid, 300, 300)));
        return encodedImage;
    }

    /**
     * Formats a JSON with all information required by the template to generate a Purchase Order Document
     * 
     * @param acquisitionRequest
     * @param requestID
     * @param supplierContact
     * @param deliveryLocalList
     * @param acquisitionRequestItemBeans
     * @param uuid
     * @return
     */
    private static JsonObject getPurchaseOrderDocumentJson(final AcquisitionRequest acquisitionRequest, final String requestID,
            final SupplierContact supplierContact, DeliveryLocalList deliveryLocalList,
            List<AcquisitionRequestItemBean> acquisitionRequestItemBeans, UUID uuid) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        final JsonObject purchaseOrderDocumentJson = new JsonObject();

        final JsonObject supplierContactjson = new JsonObject();
        if (supplierContact.getAddress() != null) {
            final JsonObject address = addressToJson(supplierContact.getAddress());
            supplierContactjson.add("address", address);
        }
        purchaseOrderDocumentJson.add("supplierContact", supplierContactjson);

        final JsonObject acquisition = new JsonObject();
        if (!Strings.isNullOrEmpty(acquisitionRequest.getAcquisitionProposalDocumentId())) {
            acquisition.addProperty("acquisitionProposalDocumentId", acquisitionRequest.getAcquisitionProposalDocumentId());
        }
        if (acquisitionRequest.getSupplier() != null) {
            final JsonObject supplier = new JsonObject();
            supplier.addProperty("fiscalIdentificationCode", acquisitionRequest.getSupplier().getFiscalIdentificationCode());
            supplier.addProperty("fax", acquisitionRequest.getSupplier().getFax());
            acquisition.add("supplier", supplier);
        }
        acquisition.addProperty("acquisitionProcessId", acquisitionRequest.getAcquisitionProcessId());
        acquisition.addProperty("totalItemValue", acquisitionRequest.getTotalItemValue().toFormatString());
        acquisition.addProperty("totalItemValueWithVat", acquisitionRequest.getTotalItemValueWithVat().toFormatString());
        acquisition.addProperty("totalVatValue", acquisitionRequest.getTotalVatValue().toFormatString());
        purchaseOrderDocumentJson.add("acquisitionRequest", acquisition);

        final JsonArray acquisitionRequestItems = new JsonArray();
        for (final AcquisitionRequestItemBean acquisitionItem : acquisitionRequestItemBeans) {
            final JsonObject acquisitionRequestItem = new JsonObject();
            acquisitionRequestItem.addProperty("proposalReference",
                    acquisitionItem.getAcquisitionRequestItem().getProposalReference());
            acquisitionRequestItem.addProperty("description", acquisitionItem.getAcquisitionRequestItem().getDescription());
            acquisitionRequestItem.addProperty("deliveryLocal", acquisitionItem.getIdentification());
            acquisitionRequestItem.addProperty("quantity", acquisitionItem.getAcquisitionRequestItem().getQuantity());
            acquisitionRequestItem.addProperty("unitValue",
                    acquisitionItem.getAcquisitionRequestItem().getUnitValue().toFormatStringWithoutCurrency());
            acquisitionRequestItem.addProperty("vatValue",
                    acquisitionItem.getAcquisitionRequestItem().getVatValue().toPlainString());
            acquisitionRequestItem.addProperty("totalItemValueWithVat",
                    acquisitionItem.getAcquisitionRequestItem().getTotalItemValueWithVat().toFormatStringWithoutCurrency());
            acquisitionRequestItems.add(acquisitionRequestItem);
        }
        purchaseOrderDocumentJson.add("acquisitionRequestItems", acquisitionRequestItems);

        final JsonArray deliveryLocalListJson = new JsonArray();
        for (final DeliveryLocal deliverLocal : deliveryLocalList) {
            final JsonObject deliveryLocal = new JsonObject();
            final JsonObject deliveryAddress = addressToJson(deliverLocal.getAddress());
            deliveryLocal.addProperty("identification", deliverLocal.getIdentification());
            deliveryLocal.addProperty("personName", deliverLocal.getPersonName());
            deliveryLocal.addProperty("email", deliverLocal.getEmail());
            deliveryLocal.addProperty("phone", deliverLocal.getPhone());
            deliveryLocal.add("address", deliveryAddress);
            deliveryLocalListJson.add(deliveryLocal);
        }
        purchaseOrderDocumentJson.add("deliveryLocalList", deliveryLocalListJson);

        acquisitionRequest.getProjectFinancersWithFundsAllocated();
        final JsonArray financersWithFundsAllocated = new JsonArray();
        for (final Financer financer : acquisitionRequest.getFinancersWithFundsAllocated()) {
            final JsonObject financerJson = new JsonObject();
            financerJson.addProperty("shortIdentifier", financer.getFinancerCostCenter().getShortIdentifier());
            financerJson.addProperty("fundAllocationid", financer.getFundAllocationId());
            financersWithFundsAllocated.add(financerJson);
        }
        purchaseOrderDocumentJson.add("financersWithFundsAllocated", financersWithFundsAllocated);

        final JsonArray financersWithProjectFundsAllocated = new JsonArray();
        for (final Financer financer : acquisitionRequest.getProjectFinancersWithFundsAllocated()) {
            final JsonObject financerJson = new JsonObject();
            final ProjectFinancer pfinancer = (ProjectFinancer) financer;
            financerJson.addProperty("shortIdentifier", pfinancer.getUnit().getShortIdentifier());
            financerJson.addProperty("fundAllocationid", pfinancer.getProjectFundAllocationId());
            financersWithProjectFundsAllocated.add(financerJson);
        }
        purchaseOrderDocumentJson.add("financersWithProjectFundsAllocated", financersWithProjectFundsAllocated);

        purchaseOrderDocumentJson.addProperty("qrcodeImage", generateURIBase64QRCode(uuid.toString()));
        purchaseOrderDocumentJson.addProperty("currentDate", dateFormat.format(new Date()));
        purchaseOrderDocumentJson.addProperty("requestID", requestID);
        purchaseOrderDocumentJson.addProperty("responsibleName", Authenticate.getUser().getProfile().getFullName());
        purchaseOrderDocumentJson.addProperty("institutionSocialSecurityNumber", ExpenditureConfiguration.get().ssn());
        purchaseOrderDocumentJson.addProperty("commitmentNumbers", acquisitionRequest.getCommitmentNumbers());
        purchaseOrderDocumentJson.addProperty("uuid", uuid.toString());
        purchaseOrderDocumentJson.addProperty("institutionSocialSecurityNumber", ExpenditureConfiguration.get().ssn());
        purchaseOrderDocumentJson.addProperty("cae", ExpenditureConfiguration.get().cae());

        return purchaseOrderDocumentJson;
    }

    /**
     * Transforms a module.finance.util.Address into a JSON representing the object
     * 
     * @param address2
     * @return
     */
    private static JsonObject addressToJson(Address address2) {
        final JsonObject address = new JsonObject();
        address.addProperty("line1", address2.getLine1());
        address.addProperty("line2", address2.getLine2());
        address.addProperty("postalCode", address2.getPostalCode());
        address.addProperty("location", address2.getLocation());
        address.addProperty("country", address2.getCountry());
        return address;
    }
}
