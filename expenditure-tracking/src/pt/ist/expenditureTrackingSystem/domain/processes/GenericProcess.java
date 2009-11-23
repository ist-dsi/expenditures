package pt.ist.expenditureTrackingSystem.domain.processes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowLog;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowProcessComment;
import module.workflow.domain.WorkflowSystem;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;

import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProcessesThatAreAuthorizedByUserPredicate;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.services.Service;

public abstract class GenericProcess extends GenericProcess_Base {

    public GenericProcess() {
	super();
    }

    public static <T extends GenericProcess> Set<T> getAllProcesses(Class<T> processClass, PaymentProcessYear year) {
	return year != null ? filter(processClass, null, year.getPaymentProcess()) : filter(processClass, null, WorkflowSystem
		.getInstance().getProcessesSet());
    }

    public static <T extends GenericProcess> Set<T> getAllProcess(Class<T> processClass, Predicate predicate,
	    PaymentProcessYear year) {
	return year != null ? filter(processClass, predicate, year.getPaymentProcess()) : filter(processClass, predicate,
		WorkflowSystem.getInstance().getProcessesSet());
    }

    public static <T extends PaymentProcess> Set<T> getProcessesWithResponsible(Class<T> processClass, final Person person,
	    PaymentProcessYear year) {
	if (person == null) {
	    return Collections.emptySet();
	}

	Set<PaymentProcess> processes = new HashSet<PaymentProcess>();
	Set<Unit> units = new HashSet<Unit>();
	for (Authorization authorization : person.getAuthorizations()) {
	    Unit unit = authorization.getUnit();
	    units.add(unit);
	    units.addAll(unit.getAllSubUnits());
	}

	for (Unit unit : units) {
	    processes.addAll(unit.getProcesses(year));
	}
	return (Set<T>) GenericProcess.filter(processClass != null ? processClass : PaymentProcess.class,
		new ProcessesThatAreAuthorizedByUserPredicate(person), processes);
    }

    public static <T extends PaymentProcess> Set<T> getProcessesForPerson(Class<T> processClass, final Person person,
	    PaymentProcessYear year) {

	Set<T> processes = null;
	if (person.hasAnyAuthorizations()) {
	    processes = new HashSet<T>();
	    for (T process : GenericProcess.getProcessesWithResponsible(processClass, person, year)) {
		if (process.hasAnyAvailableActivitity()) {
		    processes.add(process);
		}
	    }
	} else {
	    processes = GenericProcess.getAllProcess(processClass, new Predicate() {

		@Override
		public boolean evaluate(Object arg0) {
		    GenericProcess process = (GenericProcess) arg0;
		    return process.hasAnyAvailableActivity(person);
		}

	    }, year);
	}

	return processes;
    }

    public abstract <T extends GenericProcess> AbstractActivity<T> getActivityByName(String name);

    public abstract boolean hasAnyAvailableActivitity();

    public boolean hasAnyAvailableActivity(Person person) {
	final UserView userView = UserView.getCurrentUserView();
	boolean result = false;
	try {
	    userView.mockUser(person.getUser());
	    result = hasAnyAvailableActivitity();
	} finally {
	    userView.unmockUser();
	}
	return result;
    }

    public DateTime getDateFromLastActivity() {
	List<WorkflowLog> logs = new ArrayList<WorkflowLog>();
	logs.addAll(getExecutionLogs());
	Collections.sort(logs, new Comparator<WorkflowLog>() {

	    public int compare(WorkflowLog log1, WorkflowLog log2) {
		return -1 * log1.getWhenOperationWasRan().compareTo(log2.getWhenOperationWasRan());
	    }

	});

	return logs.isEmpty() ? null : logs.get(0).getWhenOperationWasRan();
    }

    public static boolean isCreateNewProcessAvailable() {
	final User user = UserView.getCurrentUser();
	return user != null;
    }

    public WorkflowProcessComment getMostRecentComment() {
	TreeSet<WorkflowProcessComment> comments = new TreeSet<WorkflowProcessComment>(WorkflowProcessComment.REVERSE_COMPARATOR);
	comments.addAll(getComments());
	return comments.size() > 0 ? comments.first() : null;
    }

    private boolean match(Class[] classes, String name) {
	for (Class clazz : classes) {
	    if (clazz.getSimpleName().equals(name)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public void notifyUserDueToComment(User user, String comment) {
	notifyPersonDueToComment(user.getExpenditurePerson(), comment);
    }

    public void notifyPersonDueToComment(Person person, String comment) {
	List<String> toAddress = new ArrayList<String>();
	toAddress.clear();
	final String email = person.getEmail();
	if (email != null) {
	    toAddress.add(email);

	    // new Email("Central de Compras", "noreply@ist.utl.pt", new
	    // String[] {}, toAddress, Collections.EMPTY_LIST,
	    // Collections.EMPTY_LIST, "New Process Comment",
	    // "There's a comment directed to you");
	}
    }

    @Service
    public void addFile(String displayName, String filename, byte[] consumeInputStream) {
	GenericFile file = new GenericFile(displayName, filename, consumeInputStream);
	addFiles2(file);
    }

    protected Person getLoggedPerson() {
	return Person.getLoggedPerson();
    }

    public List<WorkflowProcessComment> getUnreadCommentsForCurrentUser() {
	return getUnreadCommentsForPerson(Person.getLoggedPerson());
    }

    public List<WorkflowProcessComment> getUnreadCommentsForPerson(Person person) {
	User user = person.getUser();
	List<WorkflowProcessComment> comments = new ArrayList<WorkflowProcessComment>();
	for (WorkflowProcessComment comment : getComments()) {
	    if (comment.isUnreadBy(user)) {
		comments.add(comment);
	    }
	}
	return comments;
    }

    public boolean hasActivitiesFromUser(Person person) {
	User user = person.getUser();
	for (WorkflowLog log : getExecutionLogs()) {
	    if (log.getActivityExecutor() == user) {
		return true;
	    }
	}
	return false;
    }

    /*
     * TODO: Implement this methods correctly
     */
    public <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> List<T> getActivities() {
	return Collections.EMPTY_LIST;
    }

    public boolean isActive() {
	return true;
    }

}
