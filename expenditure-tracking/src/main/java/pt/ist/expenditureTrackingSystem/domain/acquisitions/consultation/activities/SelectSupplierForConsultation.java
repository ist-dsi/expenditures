package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class SelectSupplierForConsultation extends WorkflowActivity<MultipleSupplierConsultationProcess, SelectSupplierForConsultationInformation> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.PENDING_SUPPLIER_SELECTION
                && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user);
    }

    @Override
    protected void process(final SelectSupplierForConsultationInformation information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        information.getPart().setSupplier(information.getSupplier());
        if (process.getConsultation().getPartSet().stream().allMatch(p -> p.getSupplier() != null)) {
            process.setState(MultipleSupplierConsultationProcessState.SUPPLIERS_SELECTED);
        }
    }

    @Override
    public ActivityInformation<MultipleSupplierConsultationProcess> getActivityInformation( final MultipleSupplierConsultationProcess process) {
        return new SelectSupplierForConsultationInformation(process, this);
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
