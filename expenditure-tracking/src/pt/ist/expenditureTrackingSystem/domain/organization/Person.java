package pt.ist.expenditureTrackingSystem.domain.organization;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;

public class Person extends Person_Base {
    
    public Person() {
        super();
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
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
    
}
