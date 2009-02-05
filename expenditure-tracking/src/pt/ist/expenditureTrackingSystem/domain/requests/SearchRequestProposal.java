package pt.ist.expenditureTrackingSystem.domain.requests;

import java.util.Collection;
import java.util.Set;

import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.Search;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

public class SearchRequestProposal extends Search<RequestForProposalProcess> {

    private LocalDate publishDate;
    private LocalDate expireDate;
    private String requestor;
    private String title;

    private class SearchRequestProposalResult extends SearchResultSet<RequestForProposalProcess> {

	public SearchRequestProposalResult(Collection<? extends RequestForProposalProcess> c) {
	    super(c);
	}

	@Override
	protected boolean matchesSearchCriteria(final RequestForProposalProcess requestProcess) {
	    RequestForProposal proposal = requestProcess.getRequestForProposal();

	    Person person = Person.getLoggedPerson();

	    return proposal.getRequestForProposalProcess().isVisible(person)
		    && matchCriteria(proposal.getPublishDate(), proposal.getExpireDate(), proposal.getRequester().getName(),
			    proposal.getTitle());
	}

	private boolean matchCriteria(LocalDate publishDate, LocalDate expireDate, String name, String title) {
	    return (getPublishDate() == null || getPublishDate().isBefore(publishDate))
		    && (getExpireDate() == null || getExpireDate().isAfter(expireDate))
		    && (getTitle() == null || isMatch(title.toLowerCase(), getTitle().toLowerCase()))
		    && (StringUtils.isEmpty(getRequestor()) || isMatch(name.toLowerCase(), getRequestor().toLowerCase()));
	}

	private boolean isMatch(String fullName, String inputName) {
	    String[] inputNameArray = inputName.split(" ");

	    for (String inputString : inputNameArray) {
		if (fullName.indexOf(inputString) < 0) {
		    return false;
		}
	    }
	    return true;
	}

    }

    @Override
    public Set<RequestForProposalProcess> search() {
	return new SearchRequestProposalResult(GenericProcess.getAllProcesses(RequestForProposalProcess.class));
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

    public String getRequestor() {
	return requestor;
    }

    public void setRequestor(String requestor) {
	this.requestor = requestor;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

}
