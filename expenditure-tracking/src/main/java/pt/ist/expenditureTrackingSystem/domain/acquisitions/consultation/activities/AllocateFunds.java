package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class AllocateFunds extends WorkflowActivity<MultipleSupplierConsultationProcess, AllocateFundsInformation> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.SUBMITTED_FOR_FUNDS_ALLOCATION
                && process.getConsultation().getFinancerSet().stream().anyMatch(f -> f.isPendingFundAllocation() && f.isUnitFundAllocator(user));
    }

    @Override
    protected void process(final AllocateFundsInformation information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        information.getFinancer().setFundAllocation(information.getFundAllocation());
        if (process.getConsultation().getFinancerSet().stream().allMatch(f -> !f.isPendingFundAllocation())) {
            process.setState(MultipleSupplierConsultationProcessState.DOCUMENTS_UNDER_ELABORATION);
        }
    }

    @Override
    public ActivityInformation<MultipleSupplierConsultationProcess> getActivityInformation(final MultipleSupplierConsultationProcess process) {
        return new AllocateFundsInformation(process, this);
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
