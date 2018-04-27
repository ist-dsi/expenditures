package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import module.finance.util.Money;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationPart;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;

public class SelectSupplierForConsultationInformation extends AddSupplierInformation {

    private static final long serialVersionUID = 1L;

    private MultipleSupplierConsultationPart part;
    private Money value;

    public SelectSupplierForConsultationInformation(final MultipleSupplierConsultationProcess process,
            final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public MultipleSupplierConsultationPart getPart() {
        return part;
    }

    public void setPart(MultipleSupplierConsultationPart part) {
        this.part = part;
    }

    public Money getValue() {
        return value;
    }

    public void setValue(Money value) {
        this.value = value;
    }

    @Override
    public boolean hasAllneededInfo() {
        return super.hasAllneededInfo() && getPart() != null && getValue() != null;
    }

}
