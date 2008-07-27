package pt.ist.expenditureTrackingSystem.domain.authorizations;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;

public class Authorization extends Authorization_Base {
    
    public Authorization() {
	super();
    }

    public Authorization(final Person person) {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setPerson(person);
    }

    @Service
    public void delete() {
	removePerson();
	removeUnit();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

    @Service
    public void changeUnit(final Unit unit) {
	setUnit(unit);
    }

}
