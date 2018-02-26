package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.JuryMemberRole;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class AddJuryMemberInformation extends ActivityInformation<MultipleSupplierConsultationProcess> {

    private static final long serialVersionUID = 1L;

    private Person person;
    private JuryMemberRole juryMemberRole;
    private Boolean isPresidentSubstitute;

    public AddJuryMemberInformation(final MultipleSupplierConsultationProcess process,
            final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public JuryMemberRole getJuryMemberRole() {
        return juryMemberRole;
    }

    public void setJuryMemberRole(JuryMemberRole juryMemberRole) {
        this.juryMemberRole = juryMemberRole;
    }

    public Boolean getIsPresidentSubstitute() {
        return isPresidentSubstitute;
    }

    public void setIsPresidentSubstitute(Boolean isPresidentSubstitute) {
        this.isPresidentSubstitute = isPresidentSubstitute;
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput();
    }

}
