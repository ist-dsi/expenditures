package pt.ist.expenditureTrackingSystem.domain.organization;

import java.util.HashSet;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.Options;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;

public class Person extends Person_Base {
    
    public Person() {
        super();
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
        new Options(this);
    }

    public Person(final String username) {
	this();
	setUsername(username);
    }

    @Service
    public static Person createPerson() {
	return new Person();
    }

    @Service
    public void delete() {
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

    @Service
    public Authorization createAuthorization() {
	return new Authorization(this);
    }

    public static Person findByUsername(final String username) {
	if (username != null && username.length() > 0) {
	    for (final Person person : ExpenditureTrackingSystem.getInstance().getPeopleSet()) {
		if (username.equals(person.getUsername())) {
		    return person;
		}
	    }
	}
	return null;
    }

    public Set<AcquisitionProcess> findAcquisitionProcessesPendingAuthorization() {
	final Set<AcquisitionProcess> result = new HashSet<AcquisitionProcess>();
	final Options options = getOptions();
	final boolean recurseSubUnits = options.getRecurseAuthorizationPendingUnits().booleanValue();
	for (final Authorization authorization : getAuthorizationsSet()) {
	    authorization.findAcquisitionProcessesPendingAuthorization(result, recurseSubUnits);
	}
	return result;
    }
    
}
