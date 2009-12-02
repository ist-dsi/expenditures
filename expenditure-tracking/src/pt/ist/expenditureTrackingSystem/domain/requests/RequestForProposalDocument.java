package pt.ist.expenditureTrackingSystem.domain.requests;


public class RequestForProposalDocument extends RequestForProposalDocument_Base {

    public RequestForProposalDocument() {
	super();
    }

    public void delete() {
	removeRequestForProposal();
	super.delete();
    }

}
