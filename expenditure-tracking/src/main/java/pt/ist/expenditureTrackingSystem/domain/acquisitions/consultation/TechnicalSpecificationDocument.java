package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

import module.workflow.util.ClassNameBundle;

@ClassNameBundle(bundle = "ExpenditureResources")
public class TechnicalSpecificationDocument extends TechnicalSpecificationDocument_Base {
    
    public TechnicalSpecificationDocument(String displayName, String filename, byte[] content) {
        super();
        init(displayName, filename, content);
    }
    
}
