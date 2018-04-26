package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;

public class IdentifyAcquisitionProcessInformation extends ActivityInformation<MultipleSupplierConsultationProcess> {

    private static final long serialVersionUID = 1L;

    private String acquisitionProcessIdentification;

    public IdentifyAcquisitionProcessInformation(final MultipleSupplierConsultationProcess process,
            final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public String getAcquisitionProcessIdentification() {
        return acquisitionProcessIdentification;
    }

    public void setAcquisitionProcessIdentification(String acquisitionProcessIdentification) {
        this.acquisitionProcessIdentification = acquisitionProcessIdentification;
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput() && getAcquisitionProcessIdentification() != null && !getAcquisitionProcessIdentification().isEmpty();
    }

}
