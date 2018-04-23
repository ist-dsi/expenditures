package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class SetContractSecretary extends WorkflowActivity<MultipleSupplierConsultationProcess, SetContractSecretaryInformation> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState().ordinal() > MultipleSupplierConsultationProcessState.SUBMITTED_FOR_VERIFICATION.ordinal()
                && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user);
    }

    @Override
    protected void process(final SetContractSecretaryInformation information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        final MultipleSupplierConsultation consultation = process.getConsultation();
        consultation.setContractSecretary(information.getPerson().getUser());
    }

    @Override
    public ActivityInformation<MultipleSupplierConsultationProcess> getActivityInformation(final MultipleSupplierConsultationProcess process) {
        return new SetContractSecretaryInformation(process, this);
    }

    @Override
    public String getUsedBundle() {
        return "resources/ExpenditureResources";
    }

    @Override
    public boolean isUserAwarenessNeeded(final MultipleSupplierConsultationProcess process, final User user) {
        final MultipleSupplierConsultation consultation = process == null ? null : process.getConsultation();
        return consultation != null && consultation.getContractSecretary() == null;
    }

}
