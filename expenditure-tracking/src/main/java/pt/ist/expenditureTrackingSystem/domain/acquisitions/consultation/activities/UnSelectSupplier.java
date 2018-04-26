package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

public class UnSelectSupplier extends WorkflowActivity<MultipleSupplierConsultationProcess, ActivityInformation<MultipleSupplierConsultationProcess>> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.SUPPLIERS_SELECTED;
    }

    @Override
    protected void process(final ActivityInformation<MultipleSupplierConsultationProcess> information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        process.setState(MultipleSupplierConsultationProcessState.CANCELLED);
        if (process.doesNotExceedSupplierLimits()) {
            process.setState(MultipleSupplierConsultationProcessState.PENDING_SUPPLIER_SELECTION);
            process.getConsultation().getPartSet().forEach(p -> {
                p.setSupplier(null);
                p.setAdjudicatedValue(null);
            });
        } else {
            throw new DomainException(Bundle.ACQUISITION, "message.multiple.consultation.supplier.limit.exceeded");
        }
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
