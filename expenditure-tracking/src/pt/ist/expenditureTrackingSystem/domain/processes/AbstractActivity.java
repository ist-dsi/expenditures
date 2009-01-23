package pt.ist.expenditureTrackingSystem.domain.processes;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.services.Service;

public abstract class AbstractActivity<T extends GenericProcess> {

    protected abstract boolean isAvailable(T process);

    protected abstract boolean isAccessible(T process);

    protected abstract void process(T process, Object... objects);

    protected boolean isCurrentUserProcessOwner(GenericProcess process) {
	Person currentOwner = process.getCurrentOwner();
	User user = getUser();
	return currentOwner == null || (user != null && user.getPerson() == currentOwner);
    }
    
    protected boolean isProcessTaken(T process) {
	return process.getCurrentOwner() != null;
    }

    protected boolean isProcessTakenByCurrentUser(T process) {
	User user = getUser();
	Person taker = process.getCurrentOwner();
	return taker != null && user != null && taker == user.getPerson();
    }

    protected void logExecution(T process, String operationName, User user) {
	process.logExecution(user.getPerson(), operationName);
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
	final User user = getUser();
	return user != null && user.getPerson().hasRoleType(roleType);
    }

    protected User getUser() {
	return UserView.getUser();
    }

    public String getName() {
	return getClass().getSimpleName();
    }

    public String getLocalizedName() {
	return RenderUtils.getResourceString("ACQUISITION_RESOURCES", "label." + getClass().getName());
    }
}
