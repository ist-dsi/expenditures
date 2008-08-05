package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.Collection;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.Search;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

public class SearchAcquisitionProcess extends Search<AcquisitionProcess> {

    private String requester;
    private AcquisitionProcessStateType acquisitionProcessStateType;
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
	    return matchCriteria(acquisitionProcessStateType, acquisitionProcess.getAcquisitionProcessStateType())
		    && matchesSearchCriteria(acquisitionRequest);
	}

	private boolean matchesSearchCriteria(final AcquisitionRequest acquisitionRequest) {
	    return matchCriteria(requester, acquisitionRequest.getRequester().getUsername())
		    && matchCriteria(fiscalIdentificationCode, acquisitionRequest.getSupplier().getFiscalIdentificationCode())
		    && matchCriteria(costCenter, acquisitionRequest.getCostCenter())
		    && matchCriteria(project, acquisitionRequest.getProject())
		    && matchCriteria(subproject, acquisitionRequest.getSubproject());
	}

	private boolean matchCriteria(final AcquisitionProcessStateType criteria, final AcquisitionProcessStateType value) {
	    return criteria == null || criteria == value;
	}

    }

    @Override
    public Set<AcquisitionProcess> search() {
	return new SearchResult(GenericProcess.getAllProcesses(AcquisitionProcess.class));
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
