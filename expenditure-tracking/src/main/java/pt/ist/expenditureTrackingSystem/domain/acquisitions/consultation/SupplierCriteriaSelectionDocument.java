package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

import module.workflow.util.ClassNameBundle;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;

@ClassNameBundle(bundle = "ExpenditureResources")
public class SupplierCriteriaSelectionDocument extends SupplierCriteriaSelectionDocument_Base {

    static {
        FileUploadBeanResolver.registerBeanForProcessFile(SupplierCriteriaSelectionDocument.class, WorkflowFileUploadBean.class);
    }

    public SupplierCriteriaSelectionDocument(String displayName, String filename, byte[] content) {
        super();
        init(displayName, filename, content);
    }
    
}
