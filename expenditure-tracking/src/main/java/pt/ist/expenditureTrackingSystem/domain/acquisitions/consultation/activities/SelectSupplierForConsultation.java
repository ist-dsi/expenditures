package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationPart;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

public class SelectSupplierForConsultation extends WorkflowActivity<MultipleSupplierConsultationProcess, SelectSupplierForConsultationInformation> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.PENDING_SUPPLIER_SELECTION
                && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user);
    }

    @Override
    protected void process(final SelectSupplierForConsultationInformation information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        final MultipleSupplierConsultation consultation = process.getConsultation();
        final Supplier supplier = information.getSupplier();
        if (!consultation.getSupplierSet().contains(supplier)) {
            throw new DomainException(Bundle.EXPENDITURE, "message.multiple.consultation.supplier.not.a.candidate");
        }
        final MultipleSupplierConsultationPart part = information.getPart();
        part.setSupplier(supplier);
        part.setAdjudicatedValue(information.getValue());
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
