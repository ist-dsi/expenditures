package pt.ist.expenditureTrackingSystem.domain.requests;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

public class RequestForProposalDocument extends RequestForProposalDocument_Base {
    
    public  RequestForProposalDocument() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public void delete() {
	removeRequestForProposal();
	removeExpenditureTrackingSystem();
	deleteDomainObject();
    }
    
}
