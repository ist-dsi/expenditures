package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.math.BigDecimal;

public class AdvancePaymentRequest extends AdvancePaymentRequest_Base {

    public AdvancePaymentRequest(AcquisitionRequest acquisitionRequest) {
        super();
        setRequestWithPayment(acquisitionRequest);
    }

    public static void createOrEdit(AcquisitionRequest acquisitionRequest, AdvancePaymentDocumentTemplate template,
            PaymentMethod paymentMethod, BigDecimal advancePaymentPercentage, String acquisitionJustification,
            String entityJustification) {
        AdvancePaymentRequest advancePaymentRequest = acquisitionRequest.getAdvancePaymentRequest();
        if (advancePaymentRequest == null) {
            advancePaymentRequest = new AdvancePaymentRequest(acquisitionRequest);
        }
        advancePaymentRequest.setAdvancePaymentDocumentTemplate(template);
        advancePaymentRequest.setPaymentMethod(paymentMethod);
        advancePaymentRequest.setPercentage(advancePaymentPercentage);
        advancePaymentRequest.setAcquisitionJustification(acquisitionJustification);
        advancePaymentRequest.setEntityJustification(entityJustification);
    }

    public void delete() {
        setAdvancePaymentDocumentTemplate(null);
        setRequestWithPayment(null);
        deleteDomainObject();
    }

}
