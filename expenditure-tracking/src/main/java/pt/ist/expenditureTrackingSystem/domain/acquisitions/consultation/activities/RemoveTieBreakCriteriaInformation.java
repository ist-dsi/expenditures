package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.TieBreakCriteria;

public class RemoveTieBreakCriteriaInformation extends ActivityInformation<MultipleSupplierConsultationProcess> {

    private static final long serialVersionUID = 1L;

    private TieBreakCriteria tieBreakCriteria;
    
    public RemoveTieBreakCriteriaInformation(final MultipleSupplierConsultationProcess process,
            final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public TieBreakCriteria getTieBreakCriteria() {
        return tieBreakCriteria;
    }

    public void setTieBreakCriteria(TieBreakCriteria tieBreakCriteria) {
        this.tieBreakCriteria = tieBreakCriteria;
    }

    @Override
    public boolean hasAllneededInfo() {
        return getTieBreakCriteria() != null;
    }

}
