package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document;

import module.workflow.util.ClassNameBundle;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;

@ClassNameBundle(bundle = "ExpenditureResources")
public class ProcurementProposal extends PurchaseOrder_Base {

    static {
        FileUploadBeanResolver.registerBeanForProcessFile(ProcurementProposal.class, WorkflowFileUploadBean.class);
    }

    public ProcurementProposal(final String displayName, final String filename, final byte[] content) {
        super();
        init(displayName, filename, content);
    }

}
