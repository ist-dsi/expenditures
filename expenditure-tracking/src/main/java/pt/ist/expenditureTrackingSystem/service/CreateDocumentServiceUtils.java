package pt.ist.expenditureTrackingSystem.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import pt.ist.expenditureTrackingSystem.ui.QRCodeGenerator;
import pt.ist.papyrus.PapyrusClient;
import pt.ist.papyrus.PapyrusSettings;

public class CreateDocumentServiceUtils {

    protected static InputStream addSignatureFieldOnLastPage(InputStream pdfDocumentBytes, String signatureField, float x, float y,
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

    protected static PdfFormField getSignatureField(PdfWriter writer, int pageNumber, String fieldName, float x, float y,
            float width, float height) {
        final PdfFormField signature = PdfFormField.createSignature(writer);
        signature.put(PdfName.FT, PdfName.SIG);
        signature.put(PdfName.TYPE, PdfName.ANNOT);
        signature.put(PdfName.SUBTYPE, PdfName.WIDGET);
        signature.put(PdfName.P, writer.getPageReference(pageNumber));
        signature.put(PdfName.RECT, new PdfRectangle(x, y, x + width, y + height));
        signature.put(PdfName.T, new PdfString(fieldName, PdfObject.TEXT_UNICODE));
        signature.put(PdfName.F, new PdfNumber(4));
        final int offset = 15;
        signature.setWidget(new Rectangle(x, y - offset, x + width, y - offset + height), PdfAnnotation.HIGHLIGHT_INVERT);
        return signature;
    }
    
    protected static String generateURIBase64QRCode(String uuid) {
        String encodedImage = "data:image/png;base64,";
        encodedImage += Base64.getEncoder().encodeToString((QRCodeGenerator.generate(uuid, 300, 300)));
        return encodedImage;
    }

}
