package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.util.DomainReference;

public class AuthorizationBean implements Serializable {

    DomainReference<Authorization> authorization;
    DomainReference<Person> person;
    LocalDate endDate;
    Boolean canDelegate;
    
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
    
    
}
