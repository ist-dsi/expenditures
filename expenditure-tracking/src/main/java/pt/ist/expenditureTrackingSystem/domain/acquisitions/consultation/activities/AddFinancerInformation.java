package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import java.math.BigDecimal;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class AddFinancerInformation extends ActivityInformation<MultipleSupplierConsultationProcess> {

    private static final long serialVersionUID = 1L;

    private Unit financer;
    private BigDecimal percentage;

    public AddFinancerInformation(final MultipleSupplierConsultationProcess process,
            final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public Unit getFinancer() {
        return financer;
    }

    public void setFinancer(Unit financer) {
        this.financer = financer;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput();
    }

}
