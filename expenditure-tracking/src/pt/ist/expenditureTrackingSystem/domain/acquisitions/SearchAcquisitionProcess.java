package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.util.Money;

import org.apache.commons.collections.Predicate;

import pt.ist.expenditureTrackingSystem.domain.Search;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixWebFramework.util.DomainReference;

public class SearchAcquisitionProcess extends Search<AcquisitionProcess> {

    private String processId;
    private String requestDocumentId;
    private DomainReference<Person> requester;
    private DomainReference<Unit> unit;
    private DomainReference<Unit> payingUnit;
    private AcquisitionProcessStateType acquisitionProcessStateType;
    private DomainReference<Supplier> supplier;
    private String proposalId;
    private DomainReference<AccountingUnit> accountingUnit;
    private Boolean hasAvailableAndAccessibleActivityForUser;
    private Boolean responsibleUnitSetOnly = Boolean.FALSE;
    private Boolean showOnlyAcquisitionsExcludedFromSupplierLimit = Boolean.FALSE;
    private Boolean showOnlyAcquisitionsWithAdditionalCosts = Boolean.FALSE;

    private Class<? extends AcquisitionProcess> clazz;

    public SearchAcquisitionProcess(Class<? extends AcquisitionProcess> clazz) {
	this.clazz = clazz;
	setAccountingUnit(null);
    }

    private class SearchResult extends SearchResultSet<AcquisitionProcess> {

	public SearchResult(Collection<? extends AcquisitionProcess> c) {
	    super(c);
	}

	@Override
	protected boolean matchesSearchCriteria(final AcquisitionProcess acquisitionProcess) {
	    final AcquisitionRequest acquisitionRequest = acquisitionProcess.getAcquisitionRequest();
	    return matchesSearchCriteria(acquisitionRequest)
		    && (acquisitionProcess.isAvailableForCurrentUser() || acquisitionProcess.isTakenByCurrentUser());
	}

	private boolean matchesSearchCriteria(final AcquisitionRequest acquisitionRequest) {
	    final Person person = acquisitionRequest.getRequester();
	    AcquisitionProcess acquisitionProcess = acquisitionRequest.getAcquisitionProcess();

	    final List<Supplier> suppliers = acquisitionRequest.getSuppliers();
	    final String identification = acquisitionRequest.getAcquisitionProcessId();
	    final String acquisitionProposalId = acquisitionRequest.getAcquisitionProposalDocumentId();
	    final String acquisitionRequestDocumentID = acquisitionProcess.hasPurchaseOrderDocument() ? acquisitionProcess
		    .getAcquisitionRequestDocumentID() : null;
	    final AcquisitionProcessStateType type = acquisitionProcess.getAcquisitionProcessStateType();
	    final Set<AccountingUnit> accountingUnits = acquisitionRequest.getAccountingUnits();
	    return matchCriteria(processId, identification) && matchCriteria(getRequester(), person)
		    && matchCriteria(getUnit(), acquisitionRequest)
		    && matchPayingUnitCriteria(getPayingUnit(), acquisitionRequest) && matchCriteria(getSupplier(), suppliers)
		    && matchCriteria(proposalId, acquisitionProposalId)
		    && matchCriteria(hasAvailableAndAccessibleActivityForUser, acquisitionRequest)
		    && matchCriteria(acquisitionProcessStateType, type) && matchCriteria(accountingUnits, getAccountingUnit())
		    && matchCriteria(requestDocumentId, acquisitionRequestDocumentID)
		    && matchShowOnlyCriteris(acquisitionRequest);

	    // && matchCriteria(costCenter, acquisitionRequest.getCostCenter());
	}

	private boolean matchCriteria(Set<AccountingUnit> accountingUnits, AccountingUnit accountingUnit) {
	    return accountingUnit == null || accountingUnits.contains(accountingUnit);
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
	    return unit == null || unit == acquisitionRequest.getRequestingUnit();
	}

	private boolean matchPayingUnitCriteria(final Unit unit, final AcquisitionRequest acquisitionRequest) {
	    return unit == null || matchCriteria(unit, acquisitionRequest.getFinancersSet());
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

	private boolean matchShowOnlyCriteris(final AcquisitionRequest acquisitionRequest) {
	    if (showOnlyAcquisitionsExcludedFromSupplierLimit.booleanValue()
		    && !acquisitionRequest.getProcess().getShouldSkipSupplierFundAllocation().booleanValue()) {
		return false;
	    }
	    if (showOnlyAcquisitionsWithAdditionalCosts.booleanValue() && !hasAdditionalCosts(acquisitionRequest)) {
		return false;
	    }
	    return true;
	}

	private boolean hasAdditionalCosts(final AcquisitionRequest acquisitionRequest) {
	    for (final AcquisitionRequestItem acquisitionRequestItem : acquisitionRequest.getAcquisitionRequestItemsSet()) {
		final Money additionalCost = acquisitionRequestItem.getAdditionalCostValue();
		if (additionalCost != null && additionalCost.isGreaterThan(Money.ZERO)) {
		    return true;
		}
	    }
	    return false;
	}

    }

    @Override
    public Set<AcquisitionProcess> search() {
	try {
	    return new SearchResult(getProcesses());
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw new Error(ex);
	}
    }

    protected Person getLoggedPerson() {
	return Person.getLoggedPerson();
    }

    private Set<? extends AcquisitionProcess> getProcesses() {
	return responsibleUnitSetOnly ? getProcessesWithResponsible(getLoggedPerson()) : GenericProcess
		.getAllProcesses(getSearchingClass());
    }

    private Set<? extends AcquisitionProcess> getProcessesWithResponsible(final Person person) {
	if (person == null) {
	    return Collections.emptySet();
	}
	return GenericProcess.getAllProcesses(getSearchingClass(), new ProcessesThatAreAuthorizedByUserPredicate(person));
    }

    protected boolean hasCriteria(final String string) {
	return string != null && !string.isEmpty();
    }

    protected boolean hasAnyCriteria() {
	return hasCriteria(processId) || getRequester() != null || getSupplier() != null || unit != null || payingUnit != null
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

    public Unit getPayingUnit() {
	return payingUnit == null ? null : payingUnit.getObject();
    }

    public void setPayingUnit(final Unit unit) {
	this.payingUnit = unit == null ? null : new DomainReference<Unit>(unit);
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

    public Boolean getResponsibleUnitSetOnly() {
	return responsibleUnitSetOnly;
    }

    public void setResponsibleUnitSetOnly(Boolean responsibleUnitSetOnly) {
	this.responsibleUnitSetOnly = responsibleUnitSetOnly;
    }

    public static class ProcessesThatAreAuthorizedByUserPredicate implements Predicate {

	private Person person;

	public ProcessesThatAreAuthorizedByUserPredicate(Person person) {
	    this.person = person;
	}

	public boolean evaluate(Object arg0) {
	    PaymentProcess process = (PaymentProcess) arg0;
	    if (process.getRequest() == null) {
		return false;
	    }
	    List<Unit> units = process.getPayingUnits();

	    boolean evaluation = false;
	    for (Unit unit : units) {
		evaluation = evaluation || evaluate(unit, process);
	    }

	    return evaluation;
	}

	private boolean evaluate(Unit unit, PaymentProcess process) {

	    if (unit.hasAuthorizationsFor(person) && process.hasAnyAvailableActivitity()) {
		return true;
	    } else {
		for (Authorization authorization : unit.getAuthorizations()) {
		    Person person = authorization.getPerson();

		    final UserView userView = UserView.getCurrentUserView();
		    try {
			userView.mockUser(person.getUser());
			if (process.hasAnyAvailableActivitity()) {
			    userView.unmockUser();
			    return false;
			}
		    } finally {
			userView.unmockUser();
		    }
		}
		return unit.hasParentUnit() && evaluate(unit.getParentUnit(), process);
	    }
	}
    }

    public AccountingUnit getAccountingUnit() {
	return accountingUnit.getObject();
    }

    public void setAccountingUnit(AccountingUnit accountingUnit) {
	this.accountingUnit = new DomainReference<AccountingUnit>(accountingUnit);
    }

    public String getRequestDocumentId() {
	return requestDocumentId;
    }

    public void setRequestDocumentId(String requestDocumentId) {
	this.requestDocumentId = requestDocumentId;
    }

    public Boolean getShowOnlyAcquisitionsExcludedFromSupplierLimit() {
	return showOnlyAcquisitionsExcludedFromSupplierLimit;
    }

    public void setShowOnlyAcquisitionsExcludedFromSupplierLimit(Boolean showOnlyAcquisitionsExcludedFromSupplierLimit) {
	this.showOnlyAcquisitionsExcludedFromSupplierLimit = showOnlyAcquisitionsExcludedFromSupplierLimit;
    }

    public Boolean getShowOnlyAcquisitionsWithAdditionalCosts() {
	return showOnlyAcquisitionsWithAdditionalCosts;
    }

    public void setShowOnlyAcquisitionsWithAdditionalCosts(Boolean showOnlyAcquisitionsWithAdditionalCosts) {
	this.showOnlyAcquisitionsWithAdditionalCosts = showOnlyAcquisitionsWithAdditionalCosts;
    }

    public Class<? extends AcquisitionProcess> getSearchingClass() {
	return clazz;
    }

}
