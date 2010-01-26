package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class CreateUnitBean implements Serializable {
    private Unit parentUnit;
    private String name;
    private String costCenter;
    private String projectCode;

    public CreateUnitBean(Unit parentUnit) {
	setParentUnit(parentUnit);
    }

    public void setParentUnit(final Unit unit) {
	this.parentUnit = unit;
    }

    public Unit getParentUnit() {
	return parentUnit;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCostCenter() {
	return costCenter;
    }

    public void setCostCenter(String costCenter) {
	this.costCenter = costCenter;
    }

    public String getProjectCode() {
	return projectCode;
    }

    public void setProjectCode(String projectCode) {
	this.projectCode = projectCode;
    }

}
