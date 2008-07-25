package pt.ist.expenditureTrackingSystem.domain.authorizations;

import pt.ist.fenixframework.pstm.Transaction;

public class Authorization extends Authorization_Base {
    
    public  Authorization() {
        super();
    }

    public void delete() {
	removePerson();
	removeUnit();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }
    
}
