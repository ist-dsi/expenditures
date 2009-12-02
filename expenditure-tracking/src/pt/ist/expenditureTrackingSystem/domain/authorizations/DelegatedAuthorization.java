package pt.ist.expenditureTrackingSystem.domain.authorizations;

import myorg.domain.util.Money;

import org.joda.time.LocalDate;

import myorg.domain.exceptions.DomainException;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.services.Service;

public class DelegatedAuthorization extends DelegatedAuthorization_Base {

    DelegatedAuthorization(Person delegatedPerson, Unit unit, Authorization authorization, Boolean canDelegate,
	    LocalDate endDate, Money maxAmount) {
	super();
	checkParameters(authorization, delegatedPerson, unit, canDelegate, endDate, maxAmount);
	setPerson(delegatedPerson);
	setAuthorization(authorization);
	setCanDelegate(canDelegate);
	setUnit(unit);
	setStartDate(new LocalDate());
	setEndDate(endDate);
	setMaxAmount(maxAmount);
    }

    private void checkParameters(Authorization authorization, Person delegatedPerson, Unit unit, Boolean canDelegate,
	    LocalDate endDate, Money maxAmount) {
	if (authorization == null || delegatedPerson == null || canDelegate == null || unit == null) {
	    throw new DomainException("error.authorization.person.and.delegation.are.required");
	}

	if (authorization.getEndDate() != null
		&& ((endDate != null && endDate.isAfter(authorization.getEndDate()) || (authorization.getEndDate() != null && endDate == null)))) {
	    throw new DomainException("error.authorization.cannot.be.delegated.after.your.end.date");
	}

	if (!unit.isSubUnit(authorization.getUnit())) {
	    throw new DomainException("error.unit.is.not.subunit.of.authorization.unit");
	}

	if (maxAmount == null || maxAmount.isGreaterThan(authorization.getMaxAmount())) {
	    throw new DomainException("error.maxAmount.cannot.be.greater.than.authorization.maxAmount");
	}

    }

    public Person getDelegatingPerson() {
	return getAuthorization().getPerson();
    }

    @Service
    public static void delegate(Authorization authorization, Person person, Unit unit, Boolean canDelegate, LocalDate endDate,
	    Money maxAmount) {
	new DelegatedAuthorization(person, unit, authorization, canDelegate, endDate, maxAmount);
    }

    @Override
    public boolean isPersonAbleToRevokeDelegatedAuthorization(Person person) {
	return getDelegatingPerson() == person || getAuthorization().isPersonAbleToRevokeDelegatedAuthorization(person);
    }

    @Override
    public void delete() {
	removeAuthorization();
	super.delete();
    }

}
