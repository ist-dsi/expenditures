package pt.ist.expenditureTrackingSystem.domain.requests;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.Search;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

public class SearchRequestProposal extends Search<RequestForProposalProcess> {

    private LocalDate publishDate;
    private LocalDate expireDate;
    private String requestor;

    private class SearchRequestProposalResult extends SearchResultSet<RequestForProposalProcess> {

	public SearchRequestProposalResult(Collection<? extends RequestForProposalProcess> c) {
	    super(c);
	}

	@Override
	protected boolean matchesSearchCriteria(final RequestForProposalProcess requestProcess) {
	    RequestForProposal proposal = requestProcess.getRequestForProposal();

	    return matchCriteria(proposal.getPublishDate(), proposal.getExpireDate(), proposal.getRequester().getName());
	}

	private boolean matchCriteria(LocalDate publishDate, LocalDate expireDate, String name) {
	    return (getPublishDate() == null || getPublishDate().isBefore(publishDate))
		    && (getExpireDate() == null || getExpireDate().isAfter(expireDate))
		    && (StringUtils.isEmpty(getRequestor()) || getRequestor().equalsIgnoreCase(name));
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

}
