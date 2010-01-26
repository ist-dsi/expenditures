package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.Search;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProcessesThatAreAuthorizedByUserPredicate;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

public class SearchRefundProcesses extends Search<RefundProcess> {

    private String processId;
    private Person requestingPerson;
    private Unit requestingUnit;
    private RefundProcessStateType refundProcessStateType;
    private AccountingUnit accountingUnit;
    private Boolean hasAvailableAndAccessibleActivityForUser;
    private Boolean responsibleUnitSetOnly = Boolean.FALSE;
    private String refundeeName;

    public SearchRefundProcesses() {
    }

    @Override
    public Set<RefundProcess> search() {
	try {
	    return new SearchResult(getProcesses());
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw new Error(ex);
	}
    }

    private Set<? extends RefundProcess> getProcesses() {
	return responsibleUnitSetOnly ? getProcessesWithResponsible(Person.getLoggedPerson()) : GenericProcess
		.getAllProcesses(RefundProcess.class);
    }

    private Set<? extends RefundProcess> getProcessesWithResponsible(final Person person) {
	if (person == null) {
	    return Collections.emptySet();
	}
	return GenericProcess.getAllProcesses(RefundProcess.class, new ProcessesThatAreAuthorizedByUserPredicate(person));
    }

    private class SearchResult extends SearchResultSet<RefundProcess> {

	public SearchResult(Collection<? extends RefundProcess> c) {
	    super(c);
	}

	@Override
	protected boolean matchesSearchCriteria(final RefundProcess refundProcess) {
	    final RefundRequest refundRequest = refundProcess.getRequest();
	    return matchesSearchCriteria(refundRequest)
		    && (refundProcess.isAvailableForCurrentUser() || refundProcess.isTakenByCurrentUser());
	}

	private boolean matchesSearchCriteria(final RefundRequest refundRequest) {
	    final Person person = refundRequest.getRequester();
	    final Set<Supplier> suppliers = refundRequest.getSuppliers();
	    final String identification = refundRequest.getAcquisitionProcessId();
	    final RefundProcessStateType type = refundRequest.getProcess().getProcessState().getRefundProcessStateType();
	    final Set<AccountingUnit> accountingUnits = refundRequest.getAccountingUnits();
	    final String refundeeName = refundRequest.getRefundee().getName();
	    return matchCriteria(processId, identification) && matchCriteria(getRequestingPerson(), person)
		    && matchCriteria(getRequestingUnit(), refundRequest)
		    && matchCriteria(hasAvailableAndAccessibleActivityForUser, refundRequest)
		    && matchCriteria(refundProcessStateType, type) && matchCriteria(accountingUnits, getAccountingUnit())
		    && match(getRefundeeName(), refundeeName);
	}

	private boolean matchCriteria(final Unit unit, final RefundRequest refundRequest) {
	    return unit == null || unit == refundRequest.getRequestingUnit()
		    || matchCriteria(unit, refundRequest.getFinancersSet());
	}

	private boolean matchCriteria(final Unit unit, final Set<Financer> financers) {
	    for (final Financer financer : financers) {
		if (unit == financer.getUnit()) {
		    return true;
		}
	    }
	    return false;
	}

	private boolean matchCriteria(final Boolean hasAvailableAndAccessibleActivityForUser, final RefundRequest refundRequest) {
	    return hasAvailableAndAccessibleActivityForUser == null || !hasAvailableAndAccessibleActivityForUser.booleanValue()
		    || isPersonAbleToExecuteActivities(refundRequest.getProcess());
	}

	private boolean isPersonAbleToExecuteActivities(final RefundProcess refundProcess) {
	    return refundProcess.hasAnyAvailableActivitity();
	}

	private boolean matchCriteria(final Person requester, final Person person) {
	    return requester == null || requester == person;
	}

	private boolean matchCriteria(Supplier supplier, Set<Supplier> suppliers) {
	    return supplier == null || suppliers.contains(supplier);
	}

	private boolean matchCriteria(RefundProcessStateType refundProcessStateType, RefundProcessStateType type) {
	    return refundProcessStateType == null || refundProcessStateType.equals(type);
	}

	private boolean matchCriteria(Set<AccountingUnit> accountingUnits, AccountingUnit accountingUnit) {
	    return accountingUnit == null || accountingUnits.contains(accountingUnit);
	}

	private boolean match(String searchString, String string) {
	    return searchString == null || searchString.length() == 0 || searchString.equalsIgnoreCase(string);
	}

	private boolean match(Person searchPerson, Person person) {
	    return searchPerson == null || searchPerson == person;
	}

	private boolean match(Unit searchUnit, Unit unit) {
	    return searchUnit == null || searchUnit == unit;
	}
    }

    public String getRefundeeName() {
	return refundeeName;
    }

    public void setRefundeeName(String refundee) {
	this.refundeeName = refundee;
    }

    public Person getRequestingPerson() {
	return requestingPerson;
    }

    public void setRequestingPerson(final Person requestingPerson) {
	this.requestingPerson = requestingPerson;
    }

    public Unit getRequestingUnit() {
	return requestingUnit;
    }

    public String getProcessId() {
	return processId;
    }

    public void setProcessId(String processId) {
	this.processId = processId;
    }

    public RefundProcessStateType getRefundProcessStateType() {
	return refundProcessStateType;
    }

    public void setRefundProcessStateType(RefundProcessStateType refundProcessStateType) {
	this.refundProcessStateType = refundProcessStateType;
    }

    public AccountingUnit getAccountingUnit() {
	return accountingUnit;
    }

    public void setAccountingUnit(final AccountingUnit accountingUnit) {
	this.accountingUnit = accountingUnit;
    }

    public Boolean getHasAvailableAndAccessibleActivityForUser() {
	return hasAvailableAndAccessibleActivityForUser;
    }

    public void setHasAvailableAndAccessibleActivityForUser(Boolean hasAvailableAndAccessibleActivityForUser) {
	this.hasAvailableAndAccessibleActivityForUser = hasAvailableAndAccessibleActivityForUser;
    }

    public Boolean getResponsibleUnitSetOnly() {
	return responsibleUnitSetOnly;
    }

    public void setResponsibleUnitSetOnly(Boolean responsibleUnitSetOnly) {
	this.responsibleUnitSetOnly = responsibleUnitSetOnly;
    }

    public void setRequestingUnit(final Unit requestingUnit) {
	this.requestingUnit = requestingUnit;
    }

    public Class<RefundProcess> getSearchingClass() {
	return RefundProcess.class;
    }

}
