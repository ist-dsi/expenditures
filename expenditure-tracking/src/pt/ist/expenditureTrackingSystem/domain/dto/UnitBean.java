package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class UnitBean implements Serializable {

    private Unit unit;

    public UnitBean() {
    }

    public UnitBean(final Unit unit) {
	setUnit(unit);
    }

    public Unit getUnit() {
	return unit;
    }

    public void setUnit(final Unit unit) {
	this.unit = unit;
    }

}
