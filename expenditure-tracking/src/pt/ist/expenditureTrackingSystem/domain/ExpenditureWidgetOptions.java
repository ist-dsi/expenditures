package pt.ist.expenditureTrackingSystem.domain;

public class ExpenditureWidgetOptions extends ExpenditureWidgetOptions_Base {

    public ExpenditureWidgetOptions() {
	this(10);
    }

    public ExpenditureWidgetOptions(int maxListSize) {
	super();
	setMaxListSize(maxListSize);
    }
}
