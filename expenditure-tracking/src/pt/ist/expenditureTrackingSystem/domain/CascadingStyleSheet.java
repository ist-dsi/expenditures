package pt.ist.expenditureTrackingSystem.domain;

import pt.ist.fenixframework.pstm.Transaction;

public class CascadingStyleSheet extends CascadingStyleSheet_Base {
    
    public CascadingStyleSheet(final Options options) {
        super();
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
        setOptions(options);
    }

    public void delete() {
	removeOptions();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

}
