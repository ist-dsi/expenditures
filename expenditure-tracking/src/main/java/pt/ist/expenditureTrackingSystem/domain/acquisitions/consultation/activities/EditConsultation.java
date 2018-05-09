package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class EditConsultation extends WorkflowActivity<MultipleSupplierConsultationProcess, EditConsultationInformation> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.IN_GENESIS
                && process.getCreator() == Authenticate.getUser();
    }

    @Override
    protected void process(final EditConsultationInformation information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        final MultipleSupplierConsultation consultation = process.getConsultation();
        final Person contractManager = information.getContractManager();
        consultation.edit(information.getDescription(), information.getMaterial(), information.getJustification(), information.getContractType(),
                information.getContractDuration(), contractManager == null ? null : contractManager.getUser(), information.getSupplierCountJustification(),
                information.getProposalDeadline(), information.getProposalValidity(), information.getCollateral(), information.getNumberOfAlternativeProposals(),
                information.getNegotiation(), information.getSpecificEvaluationMethod(), information.getEvaluationMethodJustification());
    }

    @Override
    public ActivityInformation<MultipleSupplierConsultationProcess> getActivityInformation(final MultipleSupplierConsultationProcess process) {
        return new EditConsultationInformation(process, this);
    }

    @Override
    public String getUsedBundle() {
        return "resources/ExpenditureResources";
    }

}
