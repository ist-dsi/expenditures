package pt.ist.expenditureTrackingSystem.domain;

import java.util.Comparator;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixWebFramework.security.UserView;

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
    }

    protected void checkArguments(GenericProcess process, Person person) {
	if (process == null || person == null) {
	    throw new DomainException("error.wrong.ProcessState.arguments");
	}
    }

    protected Person getPerson() {
	final User user = UserView.getUser();
	return user.getPerson();
    }

    public static final Comparator<ProcessState> COMPARATOR_BY_WHEN = new Comparator<ProcessState>() {
	public int compare(ProcessState o1, ProcessState o2) {
	    return o1.getWhenDateTime().compareTo(o2.getWhenDateTime());
	}
    };

}
