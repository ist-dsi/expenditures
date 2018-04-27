package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import static pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState.DOCUMENTS_UNDER_ELABORATION;
import static pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState.SUBMITTED_FOR_FUNDS_ALLOCATION;
import static pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState.SUBMITTED_FOR_FUND_RESERVATION;

import org.fenixedu.bennu.core.domain.User;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class UnReserveFunds extends WorkflowActivity<MultipleSupplierConsultationProcess, UnReserveFundsInformation> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        final MultipleSupplierConsultationProcessState state = process.getState();
        return (state == SUBMITTED_FOR_FUND_RESERVATION || state == SUBMITTED_FOR_FUNDS_ALLOCATION
                || (state == DOCUMENTS_UNDER_ELABORATION && !ReserveFunds.requireFundAllocationPriorToAcquisitionRequest()))
                && process.getConsultation().getFinancerSet().stream().anyMatch(f -> !f.isPendingFundReservation() && f.isUnitProjectFundAllocator(user))
                && process.getConsultation().getFinancerSet().stream().allMatch(f -> f.isPendingFundAllocation());
    }

    @Override
    protected void process(final UnReserveFundsInformation information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        information.getFinancer().setFundReservation(null);
        process.setState(SUBMITTED_FOR_FUND_RESERVATION);
    }

    @Override
    public ActivityInformation<MultipleSupplierConsultationProcess> getActivityInformation(final MultipleSupplierConsultationProcess process) {
        return new UnReserveFundsInformation(process, this);
    }

    @Override
    public String getUsedBundle() {
        return "resources/ExpenditureResources";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean isUserAwarenessNeeded(final MultipleSupplierConsultationProcess process, final User user) {
        return false;
    }

}
