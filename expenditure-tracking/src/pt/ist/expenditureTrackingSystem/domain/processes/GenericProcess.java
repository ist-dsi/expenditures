package pt.ist.expenditureTrackingSystem.domain.processes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.security.UserView;

public abstract class GenericProcess extends GenericProcess_Base {

    public GenericProcess() {
	super();
	setOjbConcreteClass(getClass().getName());
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public static <T extends GenericProcess> Set<T> getAllProcesses(Class<T> processClass) {
	Set<T> classes = new HashSet<T>();
	for (GenericProcess process : ExpenditureTrackingSystem.getInstance().getProcessesSet()) {
	    if (processClass.isAssignableFrom(process.getClass())) {
		classes.add((T) process);
	    }
	}
	return classes;
    }

    public static <T extends GenericProcess> Set<T> getAllProcesses(Class<T> processClass, Predicate predicate) {
	Set<T> classes = new HashSet<T>();
	for (GenericProcess process : ExpenditureTrackingSystem.getInstance().getProcessesSet()) {
	    if (processClass.isAssignableFrom(process.getClass()) && predicate.evaluate(process)) {
		classes.add((T) process);
	    }
	}

	return classes;
    }

    public abstract <T extends GenericProcess> AbstractActivity<T> getActivityByName(String name);

    public DateTime getDateFromLastActivity() {
	List<GenericLog> logs = new ArrayList<GenericLog>();
	logs.addAll(getExecutionLogs());
	Collections.sort(logs, new Comparator<GenericLog>() {

	    public int compare(GenericLog log1, GenericLog log2) {
		return -1 * log1.getWhenOperationWasRan().compareTo(log2.getWhenOperationWasRan());
	    }

	});

	return logs.isEmpty() ? null : logs.get(0).getWhenOperationWasRan();
    }

    public static boolean isCreateNewProcessAvailable() {
	return UserView.getUser() != null;
    }

    public ProcessComment getMostRecentComment() {
	TreeSet<ProcessComment> comments = new TreeSet<ProcessComment>(ProcessComment.REVERSE_COMPARATOR);
	comments.addAll(getComments());
	return comments.size() > 0 ? comments.first() : null;
    }

    public List<GenericLog> getExecutionLogs(DateTime begin, DateTime end) {
	return getExecutionLogs(begin, end);
    }

    public List<GenericLog> getExecutionLogs(DateTime begin, DateTime end,
	    Class... activitiesClass) {
	List<GenericLog> logs = new ArrayList<GenericLog>();
	Interval interval = new Interval(begin, end);
	for (GenericLog log : getExecutionLogs()) {
	    if (interval.contains(log.getWhenOperationWasRan())
		    && (activitiesClass.length == 0 || match(activitiesClass, log.getOperation()))) {
		logs.add(log);
	    }
	}
	return logs;
    }

    private boolean match(Class[] classes, String name) {
	for (Class clazz : classes) {
	    if (clazz.getSimpleName().equals(name)) {
		return true;
	    }
	}
	return false;
    }
}
