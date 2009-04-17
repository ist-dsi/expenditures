package pt.ist.expenditureTrackingSystem.domain.processes;

import java.util.Comparator;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class GenericLog extends GenericLog_Base {

    public static final Comparator<GenericLog> COMPARATOR_BY_WHEN = new Comparator<GenericLog>() {

	@Override
	public int compare(final GenericLog genericLog1, final GenericLog genericLog2) {
	    final DateTime when1 = genericLog1.getWhenOperationWasRan();
	    final DateTime when2 = genericLog2.getWhenOperationWasRan();
	    final int result = when1.compareTo(when2);
	    return result == 0 ? genericLog1.getIdInternal().compareTo(genericLog2.getIdInternal()) : result;
	}
	
    };

    public static final Comparator<GenericLog> COMPARATOR_BY_WHEN_REVERSED = new Comparator<GenericLog>() {

	@Override
	public int compare(final GenericLog genericLog1, final GenericLog genericLog2) {
	    return COMPARATOR_BY_WHEN.compare(genericLog2, genericLog1);
	}

    };

    protected GenericLog() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setOjbConcreteClass(getClass().getName());
    }

    public GenericLog(GenericProcess process, Person person, String operation, DateTime when) {
	this();
	init(process, person, operation, when);
    }

    protected void init(GenericProcess process, Person person, String operation, DateTime when) {
	super.setProcess(process);
	super.setOperation(operation);
	super.setExecutor(person);
	super.setWhenOperationWasRan(when);
    }

    public <T extends GenericProcess> AbstractActivity<T> getActivity() {
	return getProcess().getActivityByName(getOperation());
    }

}
