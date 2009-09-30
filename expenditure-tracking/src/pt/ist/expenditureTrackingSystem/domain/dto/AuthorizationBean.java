package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import myorg.domain.util.Money;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.util.DomainReference;

public class AuthorizationBean implements Serializable {

    private DomainReference<Authorization> authorization;
    private DomainReference<Unit> unit;
    private DomainReference<Person> person;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean canDelegate;
    private Money maxAmount;

    public AuthorizationBean(final Person person, final Unit unit) {
	setPerson(person);
	setUnit(unit);
	setAuthorization(null);
	setStartDate(new LocalDate());
	setCanDelegate(Boolean.FALSE);
    }

    public AuthorizationBean(Authorization authorization) {
	setAuthorization(authorization);
	setPerson(null);
	setCanDelegate(Boolean.FALSE);
	setEndDate(authorization.getEndDate());
    }

    public Authorization getAuthorization() {
	return authorization.getObject();
    }

    public void setAuthorization(Authorization authorization) {
	this.authorization = new DomainReference<Authorization>(authorization);
    }

    public Person getPerson() {
	return person.getObject();
    }

    public void setPerson(Person person) {
	this.person = new DomainReference<Person>(person);
    }

    public Unit getUnit() {
	return unit.getObject();
    }

    public void setUnit(Unit unit) {
	this.unit = new DomainReference<Unit>(unit);
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

}
