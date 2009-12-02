package pt.ist.expenditureTrackingSystem.domain.requests;

import org.joda.time.LocalDate;

import myorg.domain.exceptions.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateRequestForProposalProcessBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class RequestForProposal extends RequestForProposal_Base {

    public RequestForProposal(final RequestForProposalProcess requestProcess, final Person person, Unit requestingUnit) {
	super();
	checkParameters(requestProcess, person);
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setRequestForProposalProcess(requestProcess);
	setRequester(person);
	setPublishDate(new LocalDate());
	setRequestingUnit(requestingUnit);
    }

    protected void checkParameters(RequestForProposalProcess requestProcess, Person person) {
	if (requestProcess == null) {
	    throw new DomainException("error.requestForProposal.wrong.request.process");
	}
	if (person == null) {
	    throw new DomainException("error.anonymous.creation.of.requestForProposal.information.not.allowed");
	}
    }

    protected void addRequestForProposalDocument(final String filename, final byte[] documentBytes) {
	RequestForProposalDocument proposalDocument = getRequestForProposalDocument();
	if (proposalDocument == null) {
	    proposalDocument = new RequestForProposalDocument();
	    setRequestForProposalDocument(proposalDocument);
	}
	proposalDocument.setFilename(filename);
	proposalDocument.setContent(documentBytes);
    }

    public void edit(CreateRequestForProposalProcessBean requestBean, byte[] documentBytes) {
	setTitle(requestBean.getTitle());
	setDescription(requestBean.getDescription());
	setPublishDate(requestBean.getPublishDate());
	setExpireDate(requestBean.getExpireDate());
	if (documentBytes != null) {
	    addRequestForProposalDocument(requestBean.getFilename(), documentBytes);
	}
    }

}
