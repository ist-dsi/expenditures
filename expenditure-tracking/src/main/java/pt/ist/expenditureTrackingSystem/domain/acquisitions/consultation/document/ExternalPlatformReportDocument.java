package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document;

import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.ClassNameBundle;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

@ClassNameBundle(bundle = "ExpenditureResources")
public class ExternalPlatformReportDocument extends ExternalPlatformReportDocument_Base {

    static {
        FileUploadBeanResolver.registerBeanForProcessFile(ExternalPlatformReportDocument.class, WorkflowFileUploadBean.class);
    }

    public ExternalPlatformReportDocument(final String displayName, final String filename, final byte[] content) {
        super();
        init(displayName, filename, content);
    }

    @Override
    public void validateUpload(final WorkflowProcess workflowProcess) throws ProcessFileValidationException {
        final MultipleSupplierConsultationProcess process = (MultipleSupplierConsultationProcess) workflowProcess;
        if (process.getState() != MultipleSupplierConsultationProcessState.PENDING_SUPPLIER_SELECTION) {
            throw new ProcessFileValidationException("resources/ExpenditureResources", "error.not.in.phase.PENDING_SUPPLIER_SELECTION");
        }
        super.validateUpload(workflowProcess);
        if (!ExpenditureTrackingSystem.isAcquisitionCentralGroupMember()
                && !ExpenditureTrackingSystem.isExpenseAuthority(Authenticate.getUser())) {
            throw new ProcessFileValidationException("resources/ExpenditureResources", "error.only.available.to.acquisitionCentralGroupMember");
        }
    }

}
