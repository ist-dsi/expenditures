package pt.ist.expenditureTrackingSystem.domain.processes;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class GenericLog extends GenericLog_Base {

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
