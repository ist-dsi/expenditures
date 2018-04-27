package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationPartYearExecution;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;

public class RemoveMultipleSupplierConsultationPartYearExecutionInformation extends ActivityInformation<MultipleSupplierConsultationProcess> {

    private static final long serialVersionUID = 1L;

    private MultipleSupplierConsultationPartYearExecution yearExecution;
    
    public RemoveMultipleSupplierConsultationPartYearExecutionInformation(final MultipleSupplierConsultationProcess process,
            final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public MultipleSupplierConsultationPartYearExecution getYearExecution() {
        return yearExecution;
    }

    public void setYearExecution(MultipleSupplierConsultationPartYearExecution yearExecution) {
        this.yearExecution = yearExecution;
    }

    @Override
    public boolean hasAllneededInfo() {
        return getYearExecution() != null;
    }

}
