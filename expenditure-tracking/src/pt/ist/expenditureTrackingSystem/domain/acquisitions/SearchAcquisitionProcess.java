package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.Collection;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.Search;

public class SearchAcquisitionProcess extends Search<AcquisitionProcess> {

    private String requester;
    private AcquisitionProcessState acquisitionProcessState;
    private String fiscalIdentificationCode;
    private String costCenter;
    private String project;
    private String subproject;	

    private class SearchResult extends SearchResultSet<AcquisitionProcess> {

	public SearchResult(Collection<? extends AcquisitionProcess> c) {
	    super(c);
	}

	@Override
	protected boolean matchesSearchCriteria(final AcquisitionProcess acquisitionProcess) {
	    final AcquisitionRequest acquisitionRequest = acquisitionProcess.getAcquisitionRequest();
	    return matchCriteria(acquisitionProcessState, acquisitionProcess.getAcquisitionProcessState())
	    		&& matchesSearchCriteria(acquisitionRequest);
	}

	private boolean matchesSearchCriteria(final AcquisitionRequest acquisitionRequest) {
	    final AcquisitionRequestInformation acquisitionRequestInformation = acquisitionRequest.getAcquisitionRequestInformation();
	    return matchesSearchCriteria(acquisitionRequestInformation);
	}

	private boolean matchesSearchCriteria(final AcquisitionRequestInformation acquisitionRequestInformation) {
	    return matchCriteria(requester, acquisitionRequestInformation.getRequester())
	    		&& matchCriteria(fiscalIdentificationCode, acquisitionRequestInformation.getFiscalIdentificationCode())
	    		&& matchCriteria(costCenter, acquisitionRequestInformation.getCostCenter())
	    		&& matchCriteria(project, acquisitionRequestInformation.getProject())
	    		&& matchCriteria(subproject, acquisitionRequestInformation.getSubproject());
	}

	private boolean matchCriteria(final AcquisitionProcessState criteria, final AcquisitionProcessState value) {
	    return criteria == null || criteria == value;
	}

    }

    @Override
    public Set<AcquisitionProcess> search() {
	return new SearchResult(ExpenditureTrackingSystem.getInstance().getAcquisitionProcessesSet());
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public AcquisitionProcessState getAcquisitionProcessState() {
        return acquisitionProcessState;
    }

    public void setAcquisitionProcessState(AcquisitionProcessState acquisitionProcessState) {
        this.acquisitionProcessState = acquisitionProcessState;
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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getSubproject() {
        return subproject;
    }

    public void setSubproject(String subproject) {
        this.subproject = subproject;
    }

}
