package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.Collection;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.Search;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

public class SearchAcquisitionProcess extends Search<AcquisitionProcess> {

    private String requester;
    private AcquisitionProcessStateType acquisitionProcessStateType;
    private String fiscalIdentificationCode;
    private String costCenter;

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
	    final String unsername = person == null ? null : person.getUsername();
	    final Supplier supplier = acquisitionRequest.getSupplier();
	    final String fiscalIdentificationCode = supplier == null ? null : supplier.getFiscalIdentificationCode();
	    return matchCriteria(requester, unsername) && matchCriteria(fiscalIdentificationCode, fiscalIdentificationCode);
		    //&& matchCriteria(costCenter, acquisitionRequest.getCostCenter());
	}

	private boolean matchCriteria(final AcquisitionProcessStateType criteria, final AcquisitionProcessStateType value) {
	    return criteria == null || criteria == value;
	}

    }

    @Override
    public Set<AcquisitionProcess> search() {
	try {
	    return new SearchResult(GenericProcess.getAllProcesses(AcquisitionProcess.class));
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw new Error(ex);
	}
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

}
