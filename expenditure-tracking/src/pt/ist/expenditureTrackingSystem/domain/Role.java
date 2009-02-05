package pt.ist.expenditureTrackingSystem.domain;

import pt.ist.fenixWebFramework.services.Service;

public class Role extends Role_Base {

    public Role(final RoleType type) {
	super();
	setRoleType(type);
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setSystemRole(myorg.domain.groups.Role.getRole(type));
    }

    @Service
    public static Role createRole(final RoleType type) {
	return new Role(type);
    }

    public static Role getRole(final RoleType roleType) {
	for (final Role role : ExpenditureTrackingSystem.getInstance().getRoles()) {
	    if (role.getRoleType().equals(roleType)) {
		return role;
	    }
	}
	return createRole(roleType);
    }

}
