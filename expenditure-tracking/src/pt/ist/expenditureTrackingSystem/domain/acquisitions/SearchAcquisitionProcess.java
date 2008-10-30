package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.Search;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixWebFramework.util.DomainReference;

public class SearchAcquisitionProcess extends Search<AcquisitionProcess> {

    private String processId;
    private DomainReference<Person> requester;
    private DomainReference<Unit> unit;
    private AcquisitionProcessStateType acquisitionProcessStateType;
    private DomainReference<Supplier> supplier;
    private String proposalId;
    private Boolean hasAvailableAndAccessibleActivityForUser;

    private Class<? extends AcquisitionProcess> clazz;

    public SearchAcquisitionProcess(Class<? extends AcquisitionProcess> clazz) {
	this.clazz = clazz;
    }

    private class SearchResult extends SearchResultSet<AcquisitionProcess> {

	public SearchResult(Collection<? extends AcquisitionProcess> c) {
	    super(c);
	}

	@Override
	protected boolean matchesSearchCriteria(final AcquisitionProcess acquisitionProcess) {
	    final AcquisitionRequest acquisitionRequest = acquisitionProcess.getAcquisitionRequest();
	    return matchesSearchCriteria(acquisitionRequest) && acquisitionProcess.isAvailableForCurrentUser();
	}

	private boolean matchesSearchCriteria(final AcquisitionRequest acquisitionRequest) {
	    final Person person = acquisitionRequest.getRequester();
	    final List<Supplier> suppliers = acquisitionRequest.getSuppliers();
	    final String identification = acquisitionRequest.getAcquisitionProcessId();
	    final String acquisitionProposalId = acquisitionRequest.getAcquisitionProposalDocumentId();
	    final AcquisitionProcessStateType type = acquisitionRequest.getAcquisitionProcess().getAcquisitionProcessStateType();
	    return matchCriteria(processId, identification) && matchCriteria(getRequester(), person)
		    && matchCriteria(getUnit(), acquisitionRequest) && matchCriteria(getSupplier(), suppliers)
		    && matchCriteria(proposalId, acquisitionProposalId)
		    && matchCriteria(hasAvailableAndAccessibleActivityForUser, acquisitionRequest)
		    && matchCriteria(acquisitionProcessStateType, type);
	    // && matchCriteria(costCenter, acquisitionRequest.getCostCenter());
	}

	private boolean matchCriteria(AcquisitionProcessStateType acquisitionProcessStateType, AcquisitionProcessStateType type) {
	    return acquisitionProcessStateType == null || acquisitionProcessStateType.equals(type);
	}

	private boolean matchCriteria(Supplier supplier, List<Supplier> suppliers) {
	    return supplier == null || suppliers.contains(supplier);
	}

	private boolean matchCriteria(final Boolean hasAvailableAndAccessibleActivityForUser,
		final AcquisitionRequest acquisitionRequest) {
	    return hasAvailableAndAccessibleActivityForUser == null || !hasAvailableAndAccessibleActivityForUser.booleanValue()
		    || isPersonAbleToExecuteActivities(acquisitionRequest.getAcquisitionProcess());
	}

	private boolean isPersonAbleToExecuteActivities(final AcquisitionProcess acquisitionProcess) {
	    if (acquisitionProcess instanceof RegularAcquisitionProcess) {
		return ((RegularAcquisitionProcess) acquisitionProcess).isPersonAbleToExecuteActivities();
	    }
	    return false;
	}

	private boolean matchCriteria(final Unit unit, final AcquisitionRequest acquisitionRequest) {
	    return unit == null || unit == acquisitionRequest.getRequestingUnit()
		    || matchCriteria(unit, acquisitionRequest.getFinancersSet());
	}

	private boolean matchCriteria(final Unit unit, final Set<Financer> financers) {
	    for (final Financer financer : financers) {
		if (unit == financer.getUnit()) {
		    return true;
		}
	    }
	    return false;
	}

	private boolean matchCriteria(final Person requester, final Person person) {
	    return requester == null || requester == person;
	}

    }

    @Override
    public Set<AcquisitionProcess> search() {
	try {
	    return new SearchResult(GenericProcess.getAllProcesses(clazz));
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw new Error(ex);
	}
    }

    protected boolean hasCriteria(final String string) {
	return string != null && !string.isEmpty();
    }

    protected boolean hasAnyCriteria() {
	return hasCriteria(processId) || getRequester() != null || getSupplier() != null || unit != null
		|| hasCriteria(proposalId) || hasAvailableAndAccessibleActivityForUser != null;
    }

    public Person getRequester() {
	return requester == null ? null : requester.getObject();
    }

    public void setRequester(final Person requester) {
	this.requester = requester == null ? null : new DomainReference<Person>(requester);
    }

    public Supplier getSupplier() {
	return supplier == null ? null : supplier.getObject();
    }

    public void setSupplier(final Supplier supplier) {
	this.supplier = supplier == null ? null : new DomainReference<Supplier>(supplier);
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

    public Unit getUnit() {
	return unit == null ? null : unit.getObject();
    }

    public void setUnit(final Unit unit) {
	this.unit = unit == null ? null : new DomainReference<Unit>(unit);
    }

    public Boolean getHasAvailableAndAccessibleActivityForUser() {
	return hasAvailableAndAccessibleActivityForUser;
    }

    public void setHasAvailableAndAccessibleActivityForUser(Boolean hasAvailableAndAccessibleActivityForUser) {
	this.hasAvailableAndAccessibleActivityForUser = hasAvailableAndAccessibleActivityForUser;
    }

    public AcquisitionProcessStateType getAcquisitionProcessState() {
	return acquisitionProcessStateType;
    }

    public void setAcquisitionProcessState(AcquisitionProcessStateType acquisitionProcessStateType) {
	this.acquisitionProcessStateType = acquisitionProcessStateType;
    }

}
