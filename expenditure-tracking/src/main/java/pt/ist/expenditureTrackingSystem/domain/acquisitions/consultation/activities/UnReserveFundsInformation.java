package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationFinancer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;

public class UnReserveFundsInformation extends ActivityInformation<MultipleSupplierConsultationProcess> {

    private static final long serialVersionUID = 1L;

    private MultipleSupplierConsultationFinancer financer;
    
    public UnReserveFundsInformation(final MultipleSupplierConsultationProcess process,
            final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public MultipleSupplierConsultationFinancer getFinancer() {
        return financer;
    }

    public void setFinancer(MultipleSupplierConsultationFinancer financer) {
        this.financer = financer;
    }

    @Override
    public boolean hasAllneededInfo() {
        return getFinancer() != null;
    }

}
