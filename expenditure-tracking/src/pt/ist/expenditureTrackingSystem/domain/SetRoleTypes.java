/*
 * @(#)SetRoleTypes.java
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

import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Luis Cruz
 * 
 */
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
