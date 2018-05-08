package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.ClassNameBundle;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

@ClassNameBundle(bundle = "ExpenditureResources")
public class ProcurementProposal extends ProcurementProposal_Base {

    static {
        FileUploadBeanResolver.registerBeanForProcessFile(ProcurementProposal.class, WorkflowFileUploadBean.class);
    }

    public ProcurementProposal(final String displayName, final String filename, final byte[] content) {
        super();
        init(displayName, filename, content);
    }

    @Override
    public void validateUpload(WorkflowProcess workflowProcess) throws ProcessFileValidationException {
        final MultipleSupplierConsultationProcess process = (MultipleSupplierConsultationProcess) workflowProcess;
        if (process.getState() != MultipleSupplierConsultationProcessState.PENDING_ADJUDICATION) {
            throw new ProcessFileValidationException("resources/ExpenditureResources", "error.not.in.phase.PENDING_ADJUDICATION");
        }
        super.validateUpload(workflowProcess);
    }

}
