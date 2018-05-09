package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;

public class IdentifyExpenseProcessInformation extends ActivityInformation<MultipleSupplierConsultationProcess> {

    private static final long serialVersionUID = 1L;

    private String expeseProcessIdentification;
    private String acquisitionRequestNumber;

    public IdentifyExpenseProcessInformation(final MultipleSupplierConsultationProcess process,
            final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public String getExpeseProcessIdentification() {
        return expeseProcessIdentification;
    }

    public void setExpeseProcessIdentification(String expeseProcessIdentification) {
        this.expeseProcessIdentification = expeseProcessIdentification;
    }

    public String getAcquisitionRequestNumber() {
        return acquisitionRequestNumber;
    }

    public void setAcquisitionRequestNumber(String acquisitionRequestNumber) {
        this.acquisitionRequestNumber = acquisitionRequestNumber;
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput() && getExpeseProcessIdentification() != null && !getExpeseProcessIdentification().isEmpty()
                && getAcquisitionRequestNumber() != null && !getAcquisitionRequestNumber().isEmpty();
    }

}
