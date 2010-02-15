package module.workingCapital.domain;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import module.organization.domain.Person;
import myorg.domain.User;
import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class WorkingCapital extends WorkingCapital_Base {
    
    public WorkingCapital() {
        super();
        setWorkingCapitalSystem(WorkingCapitalSystem.getInstance());
    }

    public WorkingCapital(final WorkingCapitalYear workingCapitalYear, final Unit unit, final Person movementResponsible) {
	this();
	setWorkingCapitalYear(workingCapitalYear);
	setUnit(unit);
	setMovementResponsible(movementResponsible);
    }

    public WorkingCapital(final Integer year, final Unit unit, final Person movementResponsible) {
	this(WorkingCapitalYear.findOrCreate(year), unit, movementResponsible);
    }

    public SortedSet<WorkingCapitalInitialization> getSortedWorkingCapitalInitializations() {
	final SortedSet<WorkingCapitalInitialization> result = new TreeSet<WorkingCapitalInitialization>(WorkingCapitalInitialization.COMPARATOR_BY_REQUEST_CREATION);
	result.addAll(getWorkingCapitalInitializationsSet());
	return result;
    }

    public Authorization findUnitResponsible(final Person person, final Money amount) {
	final Unit unit = getUnit();
	return findUnitResponsible(person, amount, unit);
    }

    private Authorization findUnitResponsible(final Person person, final Money amount, final Unit unit) {
	if (unit != null) {
	    boolean hasAtLeastOneResponsible = false;
	    for (final Authorization authorization : unit.getAuthorizationsSet()) {
		if (authorization.isValid() && authorization.getMaxAmount().isGreaterThanOrEqual(amount)) {
		    hasAtLeastOneResponsible = true;
		    if (authorization.getPerson().getUser() == person.getUser()) {
			return authorization;
		    }
		}
	    }
	    if (!hasAtLeastOneResponsible) {
		final Unit parent = unit.getParentUnit();
		return findUnitResponsible(person, amount, parent);
	    }
	}
	return null; 
    }

    public boolean isPendingAproval() {
	for (final WorkingCapitalInitialization workingCapitalInitialization : getWorkingCapitalInitializationsSet()) {
	    if (workingCapitalInitialization.isPendingAproval()) {
		return true;
	    }
	}
	return false;
    }

    public boolean isPendingAproval(final User user) {
	for (final WorkingCapitalInitialization workingCapitalInitialization : getWorkingCapitalInitializationsSet()) {
	    if (workingCapitalInitialization.isPendingAproval(user)) {
		return true;
	    }
	}
	return false;
    }

    public boolean isPendingVerification(final User user) {
	final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstance(); 
	if (workingCapitalSystem.isAccountingMember(user)) {
	    for (final WorkingCapitalInitialization workingCapitalInitialization : getWorkingCapitalInitializationsSet()) {
		if (workingCapitalInitialization.isPendingVerification()) {
		    return true;
		}
	    }
	}
	return false;
    }

    public boolean isPendingAuthorization(User user) {
	final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstance(); 
	if (workingCapitalSystem.isManagementeMember(user)) {
	    for (final WorkingCapitalInitialization workingCapitalInitialization : getWorkingCapitalInitializationsSet()) {
		if (workingCapitalInitialization.isPendingAuthorization()) {
		    return true;
		}
	    }
	}
	return false;
    }

    public boolean isAvailable(final User user) {
	if (user == null) {
	    return false;
	}
	final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstance();
	if (user == getMovementResponsible().getUser()
		|| workingCapitalSystem.isAccountingMember(user)
		|| workingCapitalSystem.isManagementeMember(user)
		|| isTreasuryMember(user)
		|| findUnitResponsible(user.getPerson(), Money.ZERO) != null) {
	    return true;
	}
	return isRequester(user);
    }

    public User getRequester() {
	final WorkingCapitalInitialization workingCapitalInitialization = Collections.min(getWorkingCapitalInitializationsSet(), WorkingCapitalInitialization.COMPARATOR_BY_REQUEST_CREATION);
	return workingCapitalInitialization.getRequestor().getUser();
    }

    public boolean isRequester(final User user) {
	for (final WorkingCapitalInitialization workingCapitalInitialization : getWorkingCapitalInitializationsSet()) {
	    if (user == workingCapitalInitialization.getRequestor().getUser()) {
		return true;
	    }
	}
	return false;
    }

    public boolean isCanceledOrRejected() {
	for (final WorkingCapitalInitialization workingCapitalInitialization : getWorkingCapitalInitializationsSet()) {
	    if (workingCapitalInitialization.isCanceledOrRejected()) {
		return true;
	    }
	}
	return false;
    }

    public WorkingCapitalInitialization getWorkingCapitalInitialization() {
	return Collections.max(getWorkingCapitalInitializationsSet(), WorkingCapitalInitialization.COMPARATOR_BY_REQUEST_CREATION);
    }

    public boolean hasAnyPendingWorkingCapitalRequests() {
	for (final WorkingCapitalRequest workingCapitalRequest : getWorkingCapitalRequestsSet()) {
	    if (!workingCapitalRequest.isRequestProcessedByTreasury()) {
		return true;
	    }
	}
	return false;
    }

    public boolean isTreasuryMember(final User user) {
	final Unit unit = getUnit();
	return unit.isTreasuryMember(user.getExpenditurePerson());
    }

    public Money getAvailableCapital() {
	Money result = Money.ZERO;
	for (final WorkingCapitalRequest workingCapitalRequest : getWorkingCapitalRequestsSet()) {
	    if (workingCapitalRequest.isRequestProcessedByTreasury()) {
		result = result.add(workingCapitalRequest.getRequestedValue());
	    }
	}
	return result;
    }

    public WorkingCapitalTransaction getLastTransaction() {
	final Set<WorkingCapitalTransaction> workingCapitalTransactionsSet = getWorkingCapitalTransactionsSet();
	return workingCapitalTransactionsSet.isEmpty() ? null : Collections.max(workingCapitalTransactionsSet, WorkingCapitalTransaction.COMPARATOR_BY_NUMBER);
    }

    public SortedSet<WorkingCapitalTransaction> getSortedWorkingCapitalTransactions() {
	final SortedSet<WorkingCapitalTransaction> result = new TreeSet<WorkingCapitalTransaction>(WorkingCapitalTransaction.COMPARATOR_BY_NUMBER);
	result.addAll(getWorkingCapitalTransactionsSet());
	return result;
    }

}
