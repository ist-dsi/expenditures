package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import myorg.domain.util.Money;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class AuthorizationBean implements Serializable {

    private Authorization authorization;
    private Unit unit;
    private Person person;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean canDelegate;
    private Money maxAmount;
    private boolean returnToUnitInterface = false;
    private String justification;

    public AuthorizationBean(final Person person, final Unit unit, boolean returnToUnitInterface) {
	setPerson(person);
	setUnit(unit);
	setAuthorization(null);
	setStartDate(new LocalDate());
	setCanDelegate(Boolean.FALSE);
	setReturnToUnitInterface(returnToUnitInterface);
    }

    public AuthorizationBean(Authorization authorization) {
	setAuthorization(authorization);
	setPerson(null);
	setCanDelegate(Boolean.FALSE);
	setEndDate(authorization.getEndDate());
    }

    public Authorization getAuthorization() {
	return authorization;
    }

    public void setAuthorization(Authorization authorization) {
	this.authorization = authorization;
    }

    public Person getPerson() {
	return person;
    }

    public void setPerson(Person person) {
	this.person = person;
    }

    public Unit getUnit() {
	return unit;
    }

    public void setUnit(Unit unit) {
	this.unit = unit;
    }

    public Boolean getCanDelegate() {
	return canDelegate;
    }

    public void setCanDelegate(Boolean caDelegate) {
	this.canDelegate = caDelegate;
    }

    public LocalDate getEndDate() {
	return endDate;
    }

    public void setEndDate(LocalDate endDate) {
	this.endDate = endDate;
    }

    public LocalDate getStartDate() {
	return startDate;
    }

    public void setStartDate(LocalDate startDate) {
	this.startDate = startDate;
    }

    public void setMaxAmount(Money maxAmount) {
	this.maxAmount = maxAmount;
    }

    public Money getMaxAmount() {
	return maxAmount;
    }

    public boolean isReturnToUnitInterface() {
        return returnToUnitInterface;
    }

    public void setReturnToUnitInterface(boolean returnToUnitInterface) {
        this.returnToUnitInterface = returnToUnitInterface;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

}
