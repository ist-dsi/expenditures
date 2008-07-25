package pt.ist.expenditureTrackingSystem.domain.organization;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;

public class Unit extends Unit_Base {
    
    public Unit(final Unit parentUnit) {
        super();
        final ExpenditureTrackingSystem expenditureTrackingSystem = ExpenditureTrackingSystem.getInstance();
        setExpenditureTrackingSystem(expenditureTrackingSystem);
        if (parentUnit == null) {            
            setExpenditureTrackingSystemFromTopLevelUnit(expenditureTrackingSystem);
        }
        setParentUnit(parentUnit);
    }

    @Service
    public static Unit createNewUnit(final Unit unit) {
	return new Unit(unit);
    }

    @Service
    public void delete() {
	for (final Unit unit : getSubUnitsSet()) {
	    unit.delete();
	}
	for (final Authorization authorizations : getAuthorizationsSet()) {
	    authorizations.delete();
	}
	removeExpenditureTrackingSystemFromTopLevelUnit();
	removeParentUnit();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }
    
}
