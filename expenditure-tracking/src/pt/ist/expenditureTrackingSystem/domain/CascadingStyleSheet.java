package pt.ist.expenditureTrackingSystem.domain;


public class CascadingStyleSheet extends CascadingStyleSheet_Base {
    
    public CascadingStyleSheet(final Options options) {
        super();
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
        setOptions(options);
    }

    public void delete() {
	removeOptions();
	removeExpenditureTrackingSystem();
	deleteDomainObject();
    }

}
