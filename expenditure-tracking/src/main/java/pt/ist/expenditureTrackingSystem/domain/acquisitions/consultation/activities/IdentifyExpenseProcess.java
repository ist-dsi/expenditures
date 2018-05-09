package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class IdentifyExpenseProcess extends WorkflowActivity<MultipleSupplierConsultationProcess, IdentifyExpenseProcessInformation> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.SUBMITTED_FOR_EXPENSE_PROCESS_IDENTIFICATION
                && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user);
    }

    @Override
    protected void process(final IdentifyExpenseProcessInformation information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        final MultipleSupplierConsultation consultation = process.getConsultation();
        consultation.setExpenseProcessIdentification(information.getExpeseProcessIdentification());
        consultation.setAcquisitionRequestNumber(information.getAcquisitionRequestNumber());
        process.setState(MultipleSupplierConsultationProcessState.SUBMITTED_FOR_FUND_RESERVATION);
    }

    @Override
    public ActivityInformation<MultipleSupplierConsultationProcess> getActivityInformation(final MultipleSupplierConsultationProcess process) {
        return new IdentifyExpenseProcessInformation(process, this);
    }

    @Override
    public String getUsedBundle() {
        return "resources/ExpenditureResources";
    }

}
