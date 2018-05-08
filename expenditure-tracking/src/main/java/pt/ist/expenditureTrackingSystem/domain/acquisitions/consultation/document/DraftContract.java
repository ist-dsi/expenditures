package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document;

import module.workflow.util.ClassNameBundle;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;

@ClassNameBundle(bundle = "ExpenditureResources")
public class DraftContract extends PurchaseOrder_Base {

    static {
        FileUploadBeanResolver.registerBeanForProcessFile(DraftContract.class, WorkflowFileUploadBean.class);
    }

    public DraftContract(final String displayName, final String filename, final byte[] content) {
        super();
        init(displayName, filename, content);
    }
    
}
