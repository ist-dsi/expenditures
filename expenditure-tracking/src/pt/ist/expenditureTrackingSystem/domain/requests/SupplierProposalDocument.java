package pt.ist.expenditureTrackingSystem.domain.requests;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.fenixframework.pstm.Transaction;

public class SupplierProposalDocument extends SupplierProposalDocument_Base {
    
    public  SupplierProposalDocument() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public void delete() {
	removeRequestForProposal();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }
    
}
