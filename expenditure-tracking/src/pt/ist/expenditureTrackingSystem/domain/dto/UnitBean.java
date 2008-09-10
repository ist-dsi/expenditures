package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.util.DomainReference;

public class UnitBean implements Serializable {

    private DomainReference<Unit> unit;

    public UnitBean() {
    }

    public UnitBean(final Unit unit) {
	setUnit(unit);
    }

    public Unit getUnit() {
        return unit == null ? null : unit.getObject();
    }

    public void setUnit(final Unit unit) {
        this.unit = unit == null ? null : new DomainReference<Unit>(unit);
    }

}
