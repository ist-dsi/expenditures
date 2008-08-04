package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import java.util.Collections;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.services.Service;

public abstract class AbstractActivity<T> {

    protected abstract boolean isAvailable(T process);

    protected abstract boolean isAccessible(T process);

    protected abstract void process(T process, Object... objects);

    protected abstract void logExecution(T process, String operationName, User user);
    
    
    public final void execute(T process) {
	execute(process, Collections.EMPTY_LIST);
    }

    @Service
    public final void execute(T process, Object... args) {
	checkConditionsFor(process);
	logExecution(process, getName(), getUser());
	process(process, args);
    }

    public boolean isActive(T process) {
	return isAccessible(process) && isAvailable(process);
    }
    
    private void checkConditionsFor(T process) {
	if (!isAccessible(process)) {
	    throw new RuntimeException("error." + process.getClass().getSimpleName() + ".not.accessible");
	}
	if (!isAvailable(process)) {
	    throw new RuntimeException("error." + process.getClass().getSimpleName() + ".activity.not.available");
	}
    }
    
    protected boolean userHasRole(final RoleType roleType) {
	final User user = UserView.getUser();
	return user != null && user.getPerson().hasRoleType(roleType);
    }

    protected User getUser() {
	return UserView.getUser();
    }
    
    public String getName() {
	return getClass().getSimpleName();
    }
    
    
}
