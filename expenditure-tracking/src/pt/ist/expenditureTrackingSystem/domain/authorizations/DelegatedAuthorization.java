package pt.ist.expenditureTrackingSystem.domain.authorizations;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.services.Service;

public class DelegatedAuthorization extends DelegatedAuthorization_Base {

    DelegatedAuthorization(Person delegatedPerson, Authorization authorization, Boolean canDelegate) {
	super();
	checkParameters(authorization, delegatedPerson, canDelegate);
	setPerson(delegatedPerson);
	setAuthorization(authorization);
	setCanDelegate(canDelegate);
	setUnit(authorization.getUnit());
	setStartDate(new LocalDate());
    }

    private void checkParameters(Authorization authorization, Person delegatedPerson, Boolean canDelegate) {
	if (authorization == null || delegatedPerson == null || canDelegate == null) {
	    throw new DomainException("error.authorization.person.and.delegation.are.required");
	}
    }

    public Person getDelegatingPerson() {
	return getAuthorization().getPerson();
    }

    @Service
    public static void delegate(Authorization authorization, Person person, Boolean canDelegate) {
	new DelegatedAuthorization(person, authorization, canDelegate);
    }
    @Override
    public boolean isPersonAbleToRevokeDelegatedAuthorization(Person person) {
	return getDelegatingPerson() == person || getAuthorization().isPersonAbleToRevokeDelegatedAuthorization(person);
    }

}
