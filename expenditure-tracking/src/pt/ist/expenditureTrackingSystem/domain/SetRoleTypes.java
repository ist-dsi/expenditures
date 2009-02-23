package pt.ist.expenditureTrackingSystem.domain;

import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.services.Service;

public class SetRoleTypes extends SetRoleTypes_Base {
    
    public SetRoleTypes() {
        super();
    }

    @Service
    @Override
    public void executeTask() {
	for (final RoleType roleType : RoleType.values()) {
	    final Role role = Role.getRole(roleType);
	    if (role.hasSystemRole()) {
		for (final Person person : role.getPersonSet()) {
		    final User user = person.getUser();
		    if (user != null) {
			role.getSystemRole().addUsers(user);
		    }
		}
	    }
	}
    }

    @Override
    public String getLocalizedName() {
	return getClass().getName();
    }
    
}
