package pt.ist.expenditureTrackingSystem.domain.processes;

import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.services.Service;

public abstract class AbstractActivity<T extends GenericProcess> {

    protected Person getLoggedPerson() {
	final User user = UserView.getCurrentUser();
	return user == null ? null : Person.findByUsername(user.getUsername());
    }

    protected abstract boolean isAvailable(T process);

    protected abstract boolean isAccessible(T process);

    protected abstract void process(T process, Object... objects);

    protected boolean isCurrentUserProcessOwner(GenericProcess process) {
	final Person currentOwner = process.getCurrentOwner();
	final Person loggedPerson = getLoggedPerson();
	return currentOwner == null || (loggedPerson != null && loggedPerson == currentOwner);
    }
    
    protected boolean isProcessTaken(T process) {
	return process.getCurrentOwner() != null;
    }

    protected boolean isProcessTakenByCurrentUser(T process) {
	final Person loggedPerson = getLoggedPerson();
	Person taker = process.getCurrentOwner();
	return taker != null && loggedPerson != null && taker == loggedPerson;
    }

    protected void logExecution(T process, String operationName, User user) {
	process.logExecution(getLoggedPerson(), operationName);
    }

    @Service
    public final void execute(T process, Object... args) {
	checkConditionsFor(process);
	logExecution(process, getClass().getSimpleName(), getUser());
	process(process, args);
	notifyUsers(process);
    }

    protected void notifyUsers(T process) {
    }

    public boolean isActive(T process) {
	return isAccessible(process) && isAvailable(process);
    }

    private void checkConditionsFor(T process) {
	if (!isAccessible(process)) {
	    throw new ActivityException("activities.messages.exception.notAccessible", getLocalizedName());
	}
	if (!isAvailable(process)) {
	    throw new ActivityException("activities.messages.exception.notAvailable", getLocalizedName());
	}
    }

    protected boolean userHasRole(final RoleType roleType) {
	final Person person = getLoggedPerson();
	return person != null && person.hasRoleType(roleType);
    }

    protected User getUser() {
	final User user = UserView.getCurrentUser();
	return user;
    }

    public String getName() {
	return getClass().getSimpleName();
    }

    public String getLocalizedName() {
	return RenderUtils.getResourceString("ACQUISITION_RESOURCES", "label." + getClass().getName());
    }
}
