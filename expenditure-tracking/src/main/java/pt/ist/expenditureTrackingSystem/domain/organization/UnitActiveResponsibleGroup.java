/*
 * @(#)UnitActiveResponsibleGroup.java
 *
 * Copyright 2012 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.domain.organization;

import java.util.HashSet;
import java.util.Set;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.groups.PersistentGroup;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class UnitActiveResponsibleGroup extends UnitActiveResponsibleGroup_Base {

    protected UnitActiveResponsibleGroup() {
        super();
        setSystemGroupMyOrg(getMyOrg());
    }

    protected String getNameLable() {
	return "label.persistent.group.unitActiveResponsible.name";
    }

    @Override
    public String getName() {
	return BundleUtil.getStringFromResourceBundle("resources/ExpenditureOrganizationResources", getNameLable());
    }

    protected boolean isExpectedUnitType(final Unit unit) {
	return true;
    }

    @Override
    public boolean isMember(final User user) {
	if (user.hasExpenditurePerson()) {
	    final Person person = user.getExpenditurePerson();
	    for (final Authorization authorization : person.getAuthorizationsSet()) {
		if (authorization.isValid() && isExpectedUnitType(authorization.getUnit())) {
		    return true;
		}
	    }
	}
	return false;
    }

    @Override
    public Set<User> getMembers() {
	final Set<User> members = new HashSet<User>();
	for (final Authorization authorization : ExpenditureTrackingSystem.getInstance().getAuthorizationsSet()) {
	    if (authorization.isValid() && isExpectedUnitType(authorization.getUnit())) {
		members.add(authorization.getPerson().getUser());
	    }
	}
	return members;
    }

    @Service
    public static UnitActiveResponsibleGroup getInstance() {
	final UnitActiveResponsibleGroup group = (UnitActiveResponsibleGroup) PersistentGroup.getSystemGroup(UnitActiveResponsibleGroup.class);
	return group == null ? new UnitActiveResponsibleGroup() : group;
    }

}
