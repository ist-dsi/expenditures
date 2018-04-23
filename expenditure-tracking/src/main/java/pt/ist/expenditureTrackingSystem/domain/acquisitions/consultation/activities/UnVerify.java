package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class UnVerify extends WorkflowActivity<MultipleSupplierConsultationProcess, ActivityInformation<MultipleSupplierConsultationProcess>> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.SUBMITTED_FOR_FUNDS_ALLOCATION
                && (ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user)
                        || process.getConsultation().getFinancerSet().stream().anyMatch(f -> f.isUnitFundAllocator(user)))
                && process.getConsultation().getFinancerSet().stream().allMatch(f -> f.isPendingFundAllocation());
    }

    @Override
    protected void process(final ActivityInformation<MultipleSupplierConsultationProcess> information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        process.setState(MultipleSupplierConsultationProcessState.SUBMITTED_FOR_VERIFICATION);
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
