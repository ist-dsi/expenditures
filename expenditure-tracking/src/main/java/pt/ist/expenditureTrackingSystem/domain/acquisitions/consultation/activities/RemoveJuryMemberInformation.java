package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationJuryMember;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;

public class RemoveJuryMemberInformation extends ActivityInformation<MultipleSupplierConsultationProcess> {

    private static final long serialVersionUID = 1L;

    private MultipleSupplierConsultationJuryMember juryMember;
    
    public RemoveJuryMemberInformation(final MultipleSupplierConsultationProcess process,
            final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public MultipleSupplierConsultationJuryMember getJuryMember() {
        return juryMember;
    }

    public void setJuryMember(MultipleSupplierConsultationJuryMember juryMember) {
        this.juryMember = juryMember;
    }

    @Override
    public boolean hasAllneededInfo() {
        return getJuryMember() != null;
    }

}
