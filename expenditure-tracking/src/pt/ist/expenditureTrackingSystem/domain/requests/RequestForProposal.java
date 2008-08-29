package pt.ist.expenditureTrackingSystem.domain.requests;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class RequestForProposal extends RequestForProposal_Base {

    public RequestForProposal(final RequestForProposalProcess requestProcess, final Person person) {
	super();
	checkParameters(requestProcess, person);
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setRequestForProposalProcess(requestProcess);
	setRequester(person);
    }

    private void checkParameters(RequestForProposalProcess requestProcess, Person person) {
	if (requestProcess == null) {
	    throw new DomainException("error.requestForProposal.wrong.request.process");
	}
	if (person == null) {
	    throw new DomainException("error.anonymous.creation.of.requestForProposal.information.not.allowed");
	}
    }

    public void addRequestForProposalDocument(final String filename, final byte[] documentBytes) {
	RequestForProposalDocument proposalDocument = getRequestForProposalDocument();
	if (proposalDocument == null) {
	    proposalDocument = new RequestForProposalDocument();
	    setRequestForProposalDocument(proposalDocument);
	}
	proposalDocument.setFilename(filename);
	proposalDocument.setContent(documentBytes);
    }
}
