package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import module.finance.util.Money;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;

public class EditLowPriceLimitInfoInformation extends ActivityInformation<MultipleSupplierConsultationProcess> {

    private static final long serialVersionUID = 1L;

    private Money lowPriceLimit;
    private String lowPriceLimitJustification;
    private String lowPriceLimitCriteria;
    private String priceLimitJustification;
    
    public EditLowPriceLimitInfoInformation(final MultipleSupplierConsultationProcess process,
            final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
        final MultipleSupplierConsultation consultation = process.getConsultation();
        setLowPriceLimit(consultation.getLowPriceLimit());
        setLowPriceLimitJustification(consultation.getLowPriceLimitJustification());
        setLowPriceLimitCriteria(consultation.getLowPriceLimitCriteria());
        setPriceLimitJustification(consultation.getPriceLimitJustification());
    }

    public Money getLowPriceLimit() {
        return lowPriceLimit;
    }

    public void setLowPriceLimit(Money lowPriceLimit) {
        this.lowPriceLimit = lowPriceLimit;
    }

    public String getLowPriceLimitJustification() {
        return lowPriceLimitJustification;
    }

    public void setLowPriceLimitJustification(String lowPriceLimitJustification) {
        this.lowPriceLimitJustification = lowPriceLimitJustification;
    }

    public String getLowPriceLimitCriteria() {
        return lowPriceLimitCriteria;
    }

    public void setLowPriceLimitCriteria(String lowPriceLimitCriteria) {
        this.lowPriceLimitCriteria = lowPriceLimitCriteria;
    }

    public String getPriceLimitJustification() {
        return priceLimitJustification;
    }

    public void setPriceLimitJustification(String priceLimitJustification) {
        this.priceLimitJustification = priceLimitJustification;
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput() && isPresent(getPriceLimitJustification()) && 
                ((getLowPriceLimit() == null || getLowPriceLimit().isZero()) || (isPresent(getLowPriceLimitJustification()) && isPresent(getLowPriceLimitCriteria())));
    }

    private boolean isPresent(final String s) {
        return s != null && !s.isEmpty();
    }

}
