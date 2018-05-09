package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class RemoveExpenseProcessIdentification extends WorkflowActivity<MultipleSupplierConsultationProcess, ActivityInformation<MultipleSupplierConsultationProcess>> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.SUBMITTED_FOR_FUND_RESERVATION
                && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user)
                && process.getConsultation().getFinancerSet().stream().allMatch(f -> f.isPendingFundReservation());
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
