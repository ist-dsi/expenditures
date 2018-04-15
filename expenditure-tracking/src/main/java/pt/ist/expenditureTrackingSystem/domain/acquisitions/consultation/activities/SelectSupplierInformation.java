package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationPart;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;

public class SelectSupplierInformation extends AddSupplierInformation {

    private static final long serialVersionUID = 1L;

    private MultipleSupplierConsultationPart part;

    public SelectSupplierInformation(final MultipleSupplierConsultationProcess process,
            final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public MultipleSupplierConsultationPart getPart() {
        return part;
    }

    public void setPart(MultipleSupplierConsultationPart part) {
        this.part = part;
    }

    @Override
    public boolean hasAllneededInfo() {
        return super.hasAllneededInfo() && getPart() != null;
    }

}
