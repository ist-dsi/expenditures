package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import module.workflow.domain.WorkflowProcess;
import module.workflow.util.WorkflowFileUploadBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

public class SupplierCandidacyDocumentUploadBean extends WorkflowFileUploadBean {

    private Supplier supplier;

    public SupplierCandidacyDocumentUploadBean(final WorkflowProcess process) {
        super(process);
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

}
