package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class UnApprove extends WorkflowActivity<MultipleSupplierConsultationProcess, ActivityInformation<MultipleSupplierConsultationProcess>> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        final MultipleSupplierConsultationProcessState state = process.getState();
        final boolean isSubmettedForVerification = state == MultipleSupplierConsultationProcessState.SUBMITTED_FOR_VERIFICATION;
        return (isSubmettedForVerification && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user))
                || ((isSubmettedForVerification || state == MultipleSupplierConsultationProcessState.SUBMITTED_FOR_APPROVAL)
                        && process.getConsultation().getFinancerSet().stream().anyMatch(f -> f.isApproved() && f.isUnitResponsible(user))); 
    }

    @Override
    protected void process(final ActivityInformation<MultipleSupplierConsultationProcess> information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        final MultipleSupplierConsultation consultation = process.getConsultation();
        final User user = Authenticate.getUser();
        final boolean isServiceMember = ExpenditureTrackingSystem.isAcquisitionCentralGroupMember();
        consultation.getFinancerSet().stream()
            .filter(f -> isServiceMember || f.isUnitResponsible(user))
            .forEach(f -> f.setApproved(Boolean.FALSE));
        process.setState(MultipleSupplierConsultationProcessState.SUBMITTED_FOR_APPROVAL);
    }

    @Override
    public String getUsedBundle() {
        return "resources/ExpenditureResources";
    }

    @Override
    public boolean isUserAwarenessNeeded(final MultipleSupplierConsultationProcess process, final User user) {
        return false;
    }

}
