package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document;

import module.workflow.util.ClassNameBundle;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;

@ClassNameBundle(bundle = "ExpenditureResources")
public class PreliminaryReport extends PurchaseOrder_Base {

    static {
        FileUploadBeanResolver.registerBeanForProcessFile(PreliminaryReport.class, WorkflowFileUploadBean.class);
    }

    public PreliminaryReport(final String displayName, final String filename, final byte[] content) {
        super();
        init(displayName, filename, content);
    }

}
