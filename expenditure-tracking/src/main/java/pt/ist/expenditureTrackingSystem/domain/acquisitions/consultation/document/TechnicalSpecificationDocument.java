package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document;

import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.ClassNameBundle;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

@ClassNameBundle(bundle = "ExpenditureResources")
public class TechnicalSpecificationDocument extends TechnicalSpecificationDocument_Base {

    static {
        FileUploadBeanResolver.registerBeanForProcessFile(TechnicalSpecificationDocument.class, WorkflowFileUploadBean.class);
    }

    public TechnicalSpecificationDocument(String displayName, String filename, byte[] content) {
        super();
        init(displayName, filename, content);
    }

    @Override
    public void validateUpload(WorkflowProcess workflowProcess) throws ProcessFileValidationException {
        super.validateUpload(workflowProcess);
        final MultipleSupplierConsultationProcess process = (MultipleSupplierConsultationProcess) workflowProcess;
        if (process.getState() != MultipleSupplierConsultationProcessState.IN_GENESIS) {
            throw new ProcessFileValidationException("resources/ExpenditureResources", "error.not.in.genesis.phase");
        }
        if (process.getCreator() != Authenticate.getUser()) {
            throw new ProcessFileValidationException("resources/ExpenditureResources", "error.only.available.to.process.creator");
        }
    }

}
