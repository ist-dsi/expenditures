package pt.ist.expenditureTrackingSystem.domain.authorizations;

import java.util.Comparator;
import java.util.Set;

import myorg.domain.util.Money;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.AuthorizationBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;

public class Authorization extends Authorization_Base {

    public static final Comparator<Authorization> COMPARATOR_BY_NAME_AND_DATE = new Comparator<Authorization>() {

	@Override
	public int compare(final Authorization o1, final Authorization o2) {
	    final Person p1 = o1.getPerson();
	    final Person p2 = o2.getPerson();
	    final int p = p1.getName().compareTo(p2.getName());
	    if (p == 0) {
		return o1.getStartDate().compareTo(o2.getStartDate());
	    }
	    return p;
	}
	
    };

    public Authorization() {
	super();
	setOjbConcreteClass(getClass().getName());
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setStartDate(new LocalDate());
	setMaxAmount(Money.ZERO);
    }

    public Authorization(final Person person, final Unit unit) {
	this();
	setPerson(person);
	setUnit(unit);
	setCanDelegate(Boolean.FALSE);
    }

    public Authorization(final AuthorizationBean authorizationBean) {
	this(authorizationBean.getPerson(), authorizationBean.getUnit());
	setStartDate(authorizationBean.getStartDate());
	setEndDate(authorizationBean.getEndDate());
	setCanDelegate(authorizationBean.getCanDelegate());
	setMaxAmount(authorizationBean.getMaxAmount() != null ? authorizationBean.getMaxAmount() : Money.ZERO);
    }

    @Service
    public void changeUnit(final Unit unit) {
	setUnit(unit);
    }

    public void findAcquisitionProcessesPendingAuthorization(final Set<AcquisitionProcess> result, final boolean recurseSubUnits) {
	final Unit unit = getUnit();
	unit.findAcquisitionProcessesPendingAuthorization(result, recurseSubUnits);
    }

    public boolean isPersonAbleToRevokeDelegatedAuthorization(Person person) {
	return getPerson() == person;
    }

    @Override
    public Boolean getCanDelegate() {
	return super.getCanDelegate() && isValid();
    }

    @Service
    public void revoke() {
	if (!isCurrentUserAbleToRevoke()) {
	    throw new DomainException("error.person.not.authorized.to.revoke");
	}
	setEndDate(new LocalDate());
	for (DelegatedAuthorization authorization : getDelegatedAuthorizations()) {
	    authorization.revoke();
	}
    }

    @Override
    public void setEndDate(LocalDate endDate) {

	super.setEndDate(endDate);

	if (endDate != null && (super.getEndDate() == null || super.getEndDate().isAfter(endDate))) {
	    for (Authorization delegatedAuthorization : getDelegatedAuthorizations()) {
		if (delegatedAuthorization.getEndDate() == null || delegatedAuthorization.getEndDate().isAfter(endDate)) {
		    delegatedAuthorization.setEndDate(endDate);
		}
	    }
	}
    }

    public boolean isValid() {
	return getEndDate() == null || getEndDate().isAfter(new LocalDate());
    }

    public boolean isCurrentUserAbleToRevoke() {
	final Person loggedPerson = Person.getLoggedPerson();
	return loggedPerson != null && isValid() && isPersonAbleToRevokeDelegatedAuthorization(loggedPerson);
    }

    @Service
    public void delete() {
	for (final DelegatedAuthorization delegatedAuthorization : getDelegatedAuthorizationsSet()) {
	    delegatedAuthorization.delete();
	}
	removePerson();
	removeUnit();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

}
