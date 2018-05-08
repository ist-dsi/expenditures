package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.ClassNameBundle;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

@ClassNameBundle(bundle = "ExpenditureResources")
public class PurchaseOrder extends PurchaseOrder_Base {

    static {
        FileUploadBeanResolver.registerBeanForProcessFile(PurchaseOrder.class, WorkflowFileUploadBean.class);
    }

    public PurchaseOrder(final String displayName, final String filename, final byte[] content) {
        super();
        init(displayName, filename, content);
    }

    @Override
    public void validateUpload(final WorkflowProcess workflowProcess) throws ProcessFileValidationException {
        super.validateUpload(workflowProcess);
        if (!ExpenditureTrackingSystem.isAcquisitionCentralGroupMember()) {
            throw new ProcessFileValidationException("resources/ExpenditureResources", "error.only.available.to.acquisitionCentralGroupMember");
        }
    }

}
