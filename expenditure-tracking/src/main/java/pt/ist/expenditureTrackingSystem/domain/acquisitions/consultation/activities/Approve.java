package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class Approve extends WorkflowActivity<MultipleSupplierConsultationProcess, ActivityInformation<MultipleSupplierConsultationProcess>> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.SUBMITTED_FOR_APPROVAL
                && process.getConsultation().canApprove(user);
    }

    @Override
    protected void process(final ActivityInformation<MultipleSupplierConsultationProcess> information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        final MultipleSupplierConsultation consultation = process.getConsultation();
        final User user = Authenticate.getUser();
        consultation.getFinancerSet().stream()
            .filter(f -> !f.isApproved() && f.isUnitResponsible(user))
            .forEach(f -> f.setApproved(Boolean.TRUE));
        if (consultation.getFinancerSet().stream().allMatch(f -> f.isApproved())) {
            process.setState(MultipleSupplierConsultationProcessState.SUBMITTED_FOR_VERIFICATION);
        }
    }

    @Override
    public String getUsedBundle() {
        return "resources/ExpenditureResources";
    }

}
