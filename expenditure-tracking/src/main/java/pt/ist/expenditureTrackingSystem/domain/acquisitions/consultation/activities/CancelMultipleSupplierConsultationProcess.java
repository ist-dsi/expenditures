package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import static pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState.IN_GENESIS;
import static pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState.SUBMITTED_FOR_APPROVAL;
import static pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState.SUBMITTED_FOR_VERIFICATION;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class CancelMultipleSupplierConsultationProcess extends WorkflowActivity<MultipleSupplierConsultationProcess, ActivityInformation<MultipleSupplierConsultationProcess>> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        final MultipleSupplierConsultationProcessState state = process.getState();
        return (state == IN_GENESIS && process.getCreator() == Authenticate.getUser())
                || (state == SUBMITTED_FOR_APPROVAL && process.getConsultation().canApprove(user))
                || (state == SUBMITTED_FOR_VERIFICATION && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user));
    }

    @Override
    protected void process(final ActivityInformation<MultipleSupplierConsultationProcess> information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        final MultipleSupplierConsultation consultation = process.getConsultation();
        consultation.setExpenseProcessIdentification(null);
        consultation.setAcquisitionRequestNumber(null);
        process.setState(MultipleSupplierConsultationProcessState.SUBMITTED_FOR_EXPENSE_PROCESS_IDENTIFICATION);
    }

    @Override
    public ActivityInformation<MultipleSupplierConsultationProcess> getActivityInformation(final MultipleSupplierConsultationProcess process) {
        return new ActivityInformation<MultipleSupplierConsultationProcess>(process, this);
    }

    @Override
    public String getUsedBundle() {
        return "resources/ExpenditureResources";
    }

}
