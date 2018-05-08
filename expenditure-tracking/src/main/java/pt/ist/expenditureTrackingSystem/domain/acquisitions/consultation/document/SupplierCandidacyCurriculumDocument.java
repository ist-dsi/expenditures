package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.ClassNameBundle;
import module.workflow.util.FileUploadBeanResolver;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.SupplierCandidacyDocumentUploadBean;

@ClassNameBundle(bundle = "ExpenditureResources")
public class SupplierCandidacyCurriculumDocument extends SupplierCandidacyCurriculumDocument_Base {

    static {
        FileUploadBeanResolver.registerBeanForProcessFile(SupplierCandidacyCurriculumDocument.class, SupplierCandidacyDocumentUploadBean.class);
    }

    public SupplierCandidacyCurriculumDocument(final String displayName, final String filename, final byte[] content) {
        super();
        init(displayName, filename, content);
    }

    @Override
    public void validateUpload(WorkflowProcess workflowProcess) throws ProcessFileValidationException {
        final MultipleSupplierConsultationProcess process = (MultipleSupplierConsultationProcess) workflowProcess;
        if (process.getState() != MultipleSupplierConsultationProcessState.PENDING_CANDIDATE_NOTIFICATION) {
            throw new ProcessFileValidationException("resources/ExpenditureResources", "error.not.in.phase.PENDING_CANDIDATE_NOTIFICATION");
        }
        super.validateUpload(workflowProcess);
    }

}
