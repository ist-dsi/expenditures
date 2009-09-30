package pt.ist.expenditureTrackingSystem.domain.requests;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

public class SupplierProposalDocument extends SupplierProposalDocument_Base {
    
    public  SupplierProposalDocument() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public void delete() {
	removeRequestForProposal();
	removeExpenditureTrackingSystem();
	deleteDomainObject();
    }
    
}
