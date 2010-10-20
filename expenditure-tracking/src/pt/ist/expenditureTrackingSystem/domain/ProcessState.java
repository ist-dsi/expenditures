package pt.ist.expenditureTrackingSystem.domain;

import java.util.Comparator;

import myorg.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

public abstract class ProcessState extends ProcessState_Base {

    public ProcessState() {
	super();
	setOjbConcreteClass(getClass().getName());
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    protected void initFields(final GenericProcess process, final Person person) {
	setProcess(process);
	setWho(person);
	setWhenDateTime(new DateTime());
	process.setCurrentProcessState(this);
    }

    protected void checkArguments(GenericProcess process, Person person) {
	if (process == null || person == null) {
	    throw new DomainException("error.wrong.ProcessState.arguments");
	}
    }

    protected Person getPerson() {
	return Person.getLoggedPerson();
    }

    public static final Comparator<ProcessState> COMPARATOR_BY_WHEN = new Comparator<ProcessState>() {
	public int compare(ProcessState o1, ProcessState o2) {
	    int r = o1.getWhenDateTime().compareTo(o2.getWhenDateTime());
	    return r == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : r;
	}
    };

    public abstract boolean isInFinalStage();

}
