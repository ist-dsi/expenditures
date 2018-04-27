package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import static pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState.DOCUMENTS_UNDER_ELABORATION;
import static pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState.SUBMITTED_FOR_FUNDS_ALLOCATION;
import static pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState.SUBMITTED_FOR_FUND_RESERVATION;

import org.fenixedu.bennu.core.domain.User;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class ReserveFunds extends WorkflowActivity<MultipleSupplierConsultationProcess, ReserveFundsInformation> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == SUBMITTED_FOR_FUND_RESERVATION
                && process.getConsultation().getFinancerSet().stream().anyMatch(f -> f.isPendingFundReservation() && f.isUnitProjectFundAllocator(user));
    }

    @Override
    protected void process(final ReserveFundsInformation information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        information.getFinancer().setFundReservation(information.getFundReservation());
        if (process.getConsultation().getFinancerSet().stream().allMatch(f -> !f.isPendingFundReservation())) {
            MultipleSupplierConsultationProcessState nextState = requireFundAllocationPriorToAcquisitionRequest() ?
                    SUBMITTED_FOR_FUNDS_ALLOCATION : DOCUMENTS_UNDER_ELABORATION;
            process.setState(nextState);
        }
    }

    static boolean requireFundAllocationPriorToAcquisitionRequest() {
        final ExpenditureTrackingSystem instance = ExpenditureTrackingSystem.getInstance();
        return instance.getRequireFundAllocationPriorToAcquisitionRequest() != null && instance.getRequireFundAllocationPriorToAcquisitionRequest().booleanValue();
    }

    @Override
    public ActivityInformation<MultipleSupplierConsultationProcess> getActivityInformation(final MultipleSupplierConsultationProcess process) {
        return new ReserveFundsInformation(process, this);
    }

    @Override
    public String getUsedBundle() {
        return "resources/ExpenditureResources";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

}
