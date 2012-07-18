/*
 * @(#)Role.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.domain;

import pt.ist.bennu.core.domain.User;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.services.Service;
import dml.runtime.RelationAdapter;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class Role extends Role_Base {

    public static class RolePersonListener extends RelationAdapter<Role, Person> {

	@Override
	public void afterAdd(final Role role, final Person person) {
	    super.afterAdd(role, person);
	    if (role.getSystemRole() != null) {
		role.getSystemRole().addUsers(person.getUser());
	    }
	}

	@Override
	public void afterRemove(final Role role, Person person) {
	    if (role.getSystemRole() != null) {
		role.getSystemRole().removeUsers(person.getUser());
	    }
	}

    }

    static {
	Role.PersonRole.addListener(new RolePersonListener());
    }

    public Role(final RoleType type) {
	super();
	setRoleType(type);
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setSystemRole(pt.ist.bennu.core.domain.groups.Role.getRole(type));
    }

    @Service
    public static Role createRole(final RoleType type) {
	final Role role = new Role(type);
	role.setSystemRole(pt.ist.bennu.core.domain.groups.Role.getRole(type));
	return role;
    }

    @Service
    public static Role getRole(final RoleType roleType) {
	for (final Role role : ExpenditureTrackingSystem.getInstance().getRoles()) {
	    if (role.getRoleType().equals(roleType)) {
		if (!role.hasSystemRole()) {
		    role.setSystemRole(pt.ist.bennu.core.domain.groups.Role.getRole(roleType));
		}
		for (final Person person : role.getPersonSet()) {
		    final User user = person.getUser();
		    if (user != null) {
			role.getSystemRole().addUsers(user);
		    }
		}
		return role;
	    }
	}
	return createRole(roleType);
    }

    @Override
    public boolean isConnectedToCurrentHost() {
	return getExpenditureTrackingSystem() == ExpenditureTrackingSystem.getInstance();
    }

}
