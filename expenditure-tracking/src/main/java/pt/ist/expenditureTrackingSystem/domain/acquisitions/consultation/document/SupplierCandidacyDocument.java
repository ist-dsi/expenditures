package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.WorkflowFileUploadBean;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.SupplierCandidacyDocumentUploadBean;

public abstract class SupplierCandidacyDocument extends SupplierCandidacyDocument_Base {

    @Override
    public void delete() {
        setSupplier(null);
        super.delete();
    }

    @Override
    public void fillInNonDefaultFields(final WorkflowFileUploadBean workflowBean) {
        super.fillInNonDefaultFields(workflowBean);
        final SupplierCandidacyDocumentUploadBean bean = (SupplierCandidacyDocumentUploadBean) workflowBean;
        setSupplier(bean.getSupplier());
    }

    @Override
    public void validateUpload(final WorkflowProcess workflowProcess) throws ProcessFileValidationException {
        super.validateUpload(workflowProcess);
        if (!ExpenditureTrackingSystem.isAcquisitionCentralGroupMember()) {
            throw new ProcessFileValidationException("resources/ExpenditureResources", "error.SupplierCandidacyDocument.only.uploadable.by.acquisitionCentralGroupMember");
        }
        final MultipleSupplierConsultationProcess process = (MultipleSupplierConsultationProcess) workflowProcess;
        if (process.getState() != MultipleSupplierConsultationProcessState.PENDING_CANDIDATE_DOCUMENTATION) {
            throw new ProcessFileValidationException("resources/ExpenditureResources", "error.SupplierCandidacyDocument.not.in.pending.candidate.documentation.phase");
        }
    }
}
