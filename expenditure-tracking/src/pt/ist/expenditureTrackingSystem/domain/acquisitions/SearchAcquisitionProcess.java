package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.Search;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

public class SearchAcquisitionProcess extends Search<AcquisitionProcess> {

    private String processId;
    private String requester;
    private AcquisitionProcessStateType acquisitionProcessStateType;
    private String fiscalIdentificationCode;
    private String costCenter;
    private String proposalId;

    private class SearchResult extends SearchResultSet<AcquisitionProcess> {

	public SearchResult(Collection<? extends AcquisitionProcess> c) {
	    super(c);
	}

	@Override
	protected boolean matchesSearchCriteria(final AcquisitionProcess acquisitionProcess) {
	    final AcquisitionRequest acquisitionRequest = acquisitionProcess.getAcquisitionRequest();
	    return matchCriteria(acquisitionProcessStateType, acquisitionProcess.getAcquisitionProcessStateType())
		    && matchesSearchCriteria(acquisitionRequest) && acquisitionProcess.isAvailableForCurrentUser();
	}

	private boolean matchesSearchCriteria(final AcquisitionRequest acquisitionRequest) {
	    final Person person = acquisitionRequest.getRequester();
	    final String username = person == null ? null : person.getUsername();
	    final Supplier supplier = acquisitionRequest.getSupplier();
	    final String fiscalIdentificationCode = supplier == null ? null : supplier.getFiscalIdentificationCode();
	    final String identification = acquisitionRequest.getAcquisitionProcessId();
	    final String acquisitionProposalId = acquisitionRequest.getAcquisitionProposalDocumentId();
	    return matchCriteria(processId, identification)
	    		&& matchCriteria(requester, username)
	    		&& matchCriteria(fiscalIdentificationCode, fiscalIdentificationCode)
	    		&& matchCriteria(proposalId, acquisitionProposalId);
		    //&& matchCriteria(costCenter, acquisitionRequest.getCostCenter());
	}

	private boolean matchCriteria(final AcquisitionProcessStateType criteria, final AcquisitionProcessStateType value) {
	    return criteria == null || criteria == value;
	}

	private boolean matchCriteria(Integer criteria, Integer value) {
	    return criteria == null || criteria.equals(value);
	}

    }

    @Override
    public Set<AcquisitionProcess> search() {
	try {
	    return hasAnyCriteria() ?
		    new SearchResult(GenericProcess.getAllProcesses(SimplifiedProcedureProcess.class)) :
		    Collections.EMPTY_SET;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw new Error(ex);
	}
    }

    protected boolean hasCriteria(final String string) {
	return string != null && !string.isEmpty();
    }

    protected boolean hasAnyCriteria() {
	return hasCriteria(processId)
		|| hasCriteria(requester)
		|| acquisitionProcessStateType != null
		|| hasCriteria(fiscalIdentificationCode)
		|| hasCriteria(costCenter)
		|| hasCriteria(proposalId);
    }

    public String getRequester() {
	return requester;
    }

    public void setRequester(String requester) {
	this.requester = requester;
    }

    public AcquisitionProcessStateType getAcquisitionProcessState() {
	return acquisitionProcessStateType;
    }

    public void setAcquisitionProcessState(AcquisitionProcessStateType acquisitionProcessStateType) {
	this.acquisitionProcessStateType = acquisitionProcessStateType;
    }

    public String getFiscalIdentificationCode() {
	return fiscalIdentificationCode;
    }

    public void setFiscalIdentificationCode(String fiscalIdentificationCode) {
	this.fiscalIdentificationCode = fiscalIdentificationCode;
    }

    public String getCostCenter() {
	return costCenter;
    }

    public void setCostCenter(String costCenter) {
	this.costCenter = costCenter;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProposalId() {
        return proposalId;
    }

    public void setProposalId(String proposalId) {
        this.proposalId = proposalId;
    }

}
