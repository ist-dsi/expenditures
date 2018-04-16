package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.TieBreakCriteria;

public class AddTieBreakCriteria extends WorkflowActivity<MultipleSupplierConsultationProcess, AddTieBreakCriteriaInformation> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.IN_GENESIS
                && process.getCreator() == Authenticate.getUser();
    }

    @Override
    protected void process(final AddTieBreakCriteriaInformation information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        final MultipleSupplierConsultation consultation = process.getConsultation();
        new TieBreakCriteria(consultation, information.getTieBreakCriteria());
    }

    @Override
    public ActivityInformation<MultipleSupplierConsultationProcess> getActivityInformation(final MultipleSupplierConsultationProcess process) {
        return new AddTieBreakCriteriaInformation(process, this);
    }

    @Override
    public String getUsedBundle() {
        return "resources/ExpenditureResources";
    }

}
