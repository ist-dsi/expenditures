package pt.ist.expenditureTrackingSystem.domain.requests;


public class SupplierProposalDocument extends SupplierProposalDocument_Base {

    public SupplierProposalDocument() {
	super();
    }

    public void delete() {
	removeRequestForProposal();
	super.delete();
    }

}
