package module.workingCapital.domain;

import java.util.SortedSet;
import java.util.TreeSet;

import module.organization.domain.Person;
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
	    for (final Authorization authorization : unit.getAuthorizationsSet()) {
		if (authorization.getPerson().getUser() == person.getUser()
			&& authorization.isValid()
			&& authorization.getMaxAmount().isGreaterThanOrEqual(amount)) {
		    return authorization;
		}
	    }
	    final Unit parent = unit.getParentUnit();
	    return findUnitResponsible(person, amount, parent);
	}
	return null; 
    }

}
