package pt.ist.expenditureTrackingSystem.domain;

import pt.ist.fenixWebFramework.services.Service;

public class Role extends Role_Base {
    
    public Role(RoleType type) {
        super();
        setRoleType(type);
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    @Service
    public static Role createRole(RoleType type) {
	return new Role(type);
    }
    
    public static Role getRole(RoleType roleType) {
	for (Role role : ExpenditureTrackingSystem.getInstance().getRoles()) {
	    if (role.getRoleType().equals(roleType)) {
		return role;
	    }
	}
	return createRole(roleType);
    }
    
}
