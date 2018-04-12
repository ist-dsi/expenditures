package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;

public class AddTieBreakCriteriaInformation extends ActivityInformation<MultipleSupplierConsultationProcess> {

    private static final long serialVersionUID = 1L;

    private String tieBreakCriteria;

    public AddTieBreakCriteriaInformation(final MultipleSupplierConsultationProcess process,
            final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public String getTieBreakCriteria() {
        return tieBreakCriteria;
    }

    public void setTieBreakCriteria(String tieBreakCriteria) {
        this.tieBreakCriteria = tieBreakCriteria;
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput() && tieBreakCriteria != null && !tieBreakCriteria.isEmpty();
    }

}
