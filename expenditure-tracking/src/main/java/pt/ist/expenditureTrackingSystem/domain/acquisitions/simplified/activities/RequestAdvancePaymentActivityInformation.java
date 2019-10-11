package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.math.BigDecimal;
import java.util.Set;

import com.google.common.base.Strings;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AdvancePaymentDocumentTemplate;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AdvancePaymentRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentMethod;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class RequestAdvancePaymentActivityInformation extends ActivityInformation<SimplifiedProcedureProcess> {

    private AdvancePaymentDocumentTemplate template;
    private PaymentMethod paymentMethod;
    private BigDecimal percentage;
    private String acquisitionJustification;
    private String entityJustification;

    private AdvancePaymentRequest advancePaymentRequest;

    public RequestAdvancePaymentActivityInformation(SimplifiedProcedureProcess process,
            WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
        if (process.getAcquisitionRequest().getAdvancePaymentRequest() != null) {
            setAdvancePaymentRequest(advancePaymentRequest);
        }
    }

    public AdvancePaymentRequest getAdvancePaymentRequest() {
        return advancePaymentRequest;
    }

    public void setAdvancePaymentRequest(AdvancePaymentRequest advancePaymentRequest) {
        this.advancePaymentRequest = advancePaymentRequest;
        if (advancePaymentRequest != null) {
            setTemplate(advancePaymentRequest.getAdvancePaymentDocumentTemplate());
            setPaymentMethod(advancePaymentRequest.getPaymentMethod());
            setPercentage(advancePaymentRequest.getPercentage());
            setAcquisitionJustification(advancePaymentRequest.getAcquisitionJustification());
            setEntityJustification(advancePaymentRequest.getEntityJustification());
        }
    }

    public AdvancePaymentDocumentTemplate getTemplate() {
        return template;
    }

    public void setTemplate(AdvancePaymentDocumentTemplate template) {
        this.template = template;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public String getAcquisitionJustification() {
        return acquisitionJustification;
    }

    public void setAcquisitionJustification(String acquisitionJustification) {
        this.acquisitionJustification = acquisitionJustification;
    }

    public String getEntityJustification() {
        return entityJustification;
    }

    public void setEntityJustification(String entityJustification) {
        this.entityJustification = entityJustification;
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput() && getTemplate() != null && getPaymentMethod() != null
                && (!getTemplate().getNeedAcquisitionJustification() || !Strings.isNullOrEmpty(getAcquisitionJustification()))
                && (!getTemplate().getNeedEntityJustification() || !Strings.isNullOrEmpty(getEntityJustification()))
                && (!getTemplate().getPartialValue() || (getPercentage() != null));
    }

    public Set<AdvancePaymentDocumentTemplate> getAdvancePaymentDocumentTemplateSet() {
        return ExpenditureTrackingSystem.getInstance().getAdvancePaymentDocumentTemplateSet();
    }

}
