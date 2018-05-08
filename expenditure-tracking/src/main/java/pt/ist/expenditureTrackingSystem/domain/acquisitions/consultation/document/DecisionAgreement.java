package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.ClassNameBundle;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

@ClassNameBundle(bundle = "ExpenditureResources")
public class DecisionAgreement extends DecisionAgreement_Base {

    static {
        FileUploadBeanResolver.registerBeanForProcessFile(DecisionAgreement.class, WorkflowFileUploadBean.class);
    }

    public DecisionAgreement(final String displayName, final String filename, final byte[] content) {
        super();
        init(displayName, filename, content);
    }

    @Override
    public void validateUpload(WorkflowProcess workflowProcess) throws ProcessFileValidationException {
        final MultipleSupplierConsultationProcess process = (MultipleSupplierConsultationProcess) workflowProcess;
        if (process.getState() != MultipleSupplierConsultationProcessState.DOCUMENTS_UNDER_ELABORATION) {
            throw new ProcessFileValidationException("resources/ExpenditureResources", "error.not.in.phase.DOCUMENTS_UNDER_ELABORATION");
        }
        super.validateUpload(workflowProcess);
    }

}
