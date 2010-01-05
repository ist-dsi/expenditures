package module.workingCapital.domain;

import module.organization.domain.Person;
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

}
