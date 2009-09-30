package pt.ist.expenditureTrackingSystem.domain.processes;

import java.util.Comparator;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class ProcessComment extends ProcessComment_Base {

    public final static Comparator<ProcessComment> COMPARATOR = new Comparator<ProcessComment>() {

	public int compare(ProcessComment o1, ProcessComment o2) {
	    return o1.getDate().compareTo(o2.getDate());
	}

    };

    public final static Comparator<ProcessComment> REVERSE_COMPARATOR = new Comparator<ProcessComment>() {

	public int compare(ProcessComment o1, ProcessComment o2) {
	    return -1 * o1.getDate().compareTo(o2.getDate());
	}

    };

    public ProcessComment(GenericProcess process, Person commenter, String comment) {
	super();
	setComment(comment);
	setCommenter(commenter);
	setDate(new DateTime());
	setProcess(process);
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public void delete() {
	removeCommenter();
	removeProcess();
	removeExpenditureTrackingSystem();
	deleteDomainObject();
    }

    public boolean isUnreadBy(Person person) {
	return !getReaders().contains(person);
    }
}
