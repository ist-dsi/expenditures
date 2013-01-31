/*
 * @(#)ProjectActiveResponsibleGroup.java
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

import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.groups.PersistentGroup;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Pedro Santos
 * @author Luis Cruz
 * 
 */
public class ProjectActiveResponsibleGroup extends ProjectActiveResponsibleGroup_Base {

	protected ProjectActiveResponsibleGroup() {
		super();
		setSystemGroupMyOrg(MyOrg.getInstance());
	}

	@Override
	protected String getNameLable() {
		return "label.persistent.group.projectActiveResponsible.name";
	}

	@Override
	protected boolean isExpectedUnitType(Unit unit) {
		return unit instanceof Project;
	}

	@Service
	public static ProjectActiveResponsibleGroup getInstance() {
		final ProjectActiveResponsibleGroup group =
				(ProjectActiveResponsibleGroup) PersistentGroup.getSystemGroup(ProjectActiveResponsibleGroup.class);
		return group == null ? new ProjectActiveResponsibleGroup() : group;
	}

}
