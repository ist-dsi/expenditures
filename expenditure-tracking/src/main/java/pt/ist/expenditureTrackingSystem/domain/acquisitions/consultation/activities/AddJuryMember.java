package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.JuryMemberRole;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationJuryMember;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class AddJuryMember extends WorkflowActivity<MultipleSupplierConsultationProcess, AddJuryMemberInformation> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.IN_GENESIS
                && process.getCreator() == Authenticate.getUser();
    }

    @Override
    protected void process(final AddJuryMemberInformation information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        final MultipleSupplierConsultation consultation = process.getConsultation();
        final User user = information.getPerson().getUser();
        final JuryMemberRole role = information.getJuryMemberRole();
        final Boolean isPresidentSubstitute = information.getIsPresidentSubstitute();

        final MultipleSupplierConsultationJuryMember member = consultation.getJuryMemberSet().stream()
            .filter(m -> m.getUser() == user)
            .findAny()
            .orElseGet(() -> new MultipleSupplierConsultationJuryMember(consultation, user))
            ;

        member.setJuryMemberRole(role);
        if (role == JuryMemberRole.PRESIDENT) {
            consultation.getJuryMemberSet().stream()
                .filter(m -> m.getUser() != user)
                .filter(m -> m.getJuryMemberRole() == JuryMemberRole.PRESIDENT)
                .forEach(m -> m.setJuryMemberRole(JuryMemberRole.VOWEL));
                ;
        } else if (isPresidentSubstitute != null && isPresidentSubstitute) {
            consultation.setPresidentSubstitute(member);
        }
    }

    @Override
    public ActivityInformation<MultipleSupplierConsultationProcess> getActivityInformation(final MultipleSupplierConsultationProcess process) {
        return new AddJuryMemberInformation(process, this);
    }

    @Override
    public String getUsedBundle() {
        return "resources/ExpenditureResources";
    }

}
