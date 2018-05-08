package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document;

import module.workflow.util.ClassNameBundle;
import module.workflow.util.FileUploadBeanResolver;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.SupplierCandidacyDocumentUploadBean;

@ClassNameBundle(bundle = "ExpenditureResources")
public class SupplierCandidacyProposalDocument extends SupplierCandidacyProposalDocument_Base {

    static {
        FileUploadBeanResolver.registerBeanForProcessFile(SupplierCandidacyProposalDocument.class, SupplierCandidacyDocumentUploadBean.class);
    }

    public SupplierCandidacyProposalDocument(final String displayName, final String filename, final byte[] content) {
        super();
        init(displayName, filename, content);
    }
    
}
