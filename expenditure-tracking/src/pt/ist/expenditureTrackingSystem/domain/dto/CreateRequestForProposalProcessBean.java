package pt.ist.expenditureTrackingSystem.domain.dto;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.requests.RequestForProposal;
import pt.ist.expenditureTrackingSystem.domain.requests.RequestForProposalProcess;
import pt.ist.expenditureTrackingSystem.presentationTier.util.FileUploadBean;
import pt.ist.fenixWebFramework.util.DomainReference;

public class CreateRequestForProposalProcessBean extends FileUploadBean {

    private String title;
    private String description;
    private DomainReference<Unit> requestingUnit;
    private DomainReference<Person> requester;
    private LocalDate publishDate;
    private LocalDate expireDate;

    public CreateRequestForProposalProcessBean() {
	setRequestingUnit(null);
	setRequester(null);
    }
    
    public CreateRequestForProposalProcessBean(RequestForProposalProcess process) {
	RequestForProposal proposal = process.getRequestForProposal();
	setTitle(proposal.getTitle());
	setDescription(proposal.getDescription());
	setRequestingUnit(proposal.getRequestingUnit());
	setPublishDate(proposal.getPublishDate());
	setExpireDate(proposal.getExpireDate());
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Unit getRequestingUnit() {
	return requestingUnit.getObject();
    }

    public void setRequestingUnit(Unit requestingUnit) {
	this.requestingUnit = new DomainReference<Unit>(requestingUnit);
    }

    public Person getRequester() {
	return requester.getObject();
    }

    public void setRequester(Person requester) {
	this.requester = new DomainReference<Person>(requester);
    }

    public LocalDate getPublishDate() {
	return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
	this.publishDate = publishDate;
    }

    public LocalDate getExpireDate() {
	return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
	this.expireDate = expireDate;
    }

}
