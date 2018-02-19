package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

import module.workflow.util.ClassNameBundle;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;

@ClassNameBundle(bundle = "ExpenditureResources")
public class TechnicalSpecificationDocument extends TechnicalSpecificationDocument_Base {

    static {
        FileUploadBeanResolver.registerBeanForProcessFile(TechnicalSpecificationDocument.class, WorkflowFileUploadBean.class);
    }

    public TechnicalSpecificationDocument(String displayName, String filename, byte[] content) {
        super();
        init(displayName, filename, content);
    }
    
}
