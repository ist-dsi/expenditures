package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

import module.workflow.util.ClassNameBundle;

@ClassNameBundle(bundle = "ExpenditureResources")
public class SupplierCriteriaSelectionDocument extends SupplierCriteriaSelectionDocument_Base {
    
    public SupplierCriteriaSelectionDocument(String displayName, String filename, byte[] content) {
        super();
        init(displayName, filename, content);
    }
    
}
