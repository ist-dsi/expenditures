package pt.ist.expenditureTrackingSystem.domain.requests;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.fenixframework.pstm.Transaction;

public class RequestForProposalDocument extends RequestForProposalDocument_Base {
    
    public  RequestForProposalDocument() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public void delete() {
	removeRequestForProposal();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }
    
}
