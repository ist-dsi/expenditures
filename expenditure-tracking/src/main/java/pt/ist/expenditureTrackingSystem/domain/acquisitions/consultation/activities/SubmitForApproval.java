package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class SubmitForApproval extends WorkflowActivity<MultipleSupplierConsultationProcess, ActivityInformation<MultipleSupplierConsultationProcess>> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.IN_GENESIS
                && process.getCreator() == Authenticate.getUser()
                && process.doesNotExceedSupplierLimits();
    }

    @Override
    protected void process(final ActivityInformation<MultipleSupplierConsultationProcess> information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        process.setState(MultipleSupplierConsultationProcessState.SUBMITTED_FOR_APPROVAL);
    }

    @Override
    public String getUsedBundle() {
        return "resources/ExpenditureResources";
    }

}
