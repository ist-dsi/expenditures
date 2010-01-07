package module.workingCapital.domain;

import java.util.Collections;
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
		|| findUnitResponsible(user.getPerson(), Money.ZERO) != null) {
	    return true;
	}
	for (final WorkingCapitalInitialization workingCapitalInitialization : getWorkingCapitalInitializationsSet()) {
	    if (user == workingCapitalInitialization.getRequestor().getUser()) {
		return true;
	    }
	}
	return false;
    }

    public User getRequester() {
	final WorkingCapitalInitialization workingCapitalInitialization = Collections.min(getWorkingCapitalInitializationsSet(), WorkingCapitalInitialization.COMPARATOR_BY_REQUEST_CREATION);
	return workingCapitalInitialization.getRequestor().getUser();
    }

}
