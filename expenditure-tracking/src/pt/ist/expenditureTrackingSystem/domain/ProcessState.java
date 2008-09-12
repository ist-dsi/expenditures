package pt.ist.expenditureTrackingSystem.domain;

import java.util.Comparator;

public abstract class ProcessState extends ProcessState_Base {

    public ProcessState() {
	super();
	setOjbConcreteClass(getClass().getName());
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    
    public static final Comparator<ProcessState> COMPARATOR_BY_WHEN = new Comparator<ProcessState>() {
	public int compare(ProcessState o1, ProcessState o2) {
	    return o1.getWhenDateTime().compareTo(o2.getWhenDateTime());
	}
    };

}
