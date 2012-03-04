/*
 * @(#)ImportEmployeeInfoAndUpdateStructure.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package module.mission.domain.util;

import java.util.Collection;

import module.mission.domain.MissionSystem;
import module.organization.domain.Accountability;
import module.organization.domain.AccountabilityType;
import module.organization.domain.OrganizationalModel;
import module.organization.domain.Party;
import module.organization.domain.Unit;
import module.organizationIst.domain.IstAccountabilityType;
import module.organizationIst.domain.task.ImportEmployeeInfo;
import myorg.domain.MyOrg;

import org.joda.time.LocalDate;

/**
 * 
 * @author João Antunes
 * @author Luis Cruz
 * 
 */
public class ImportEmployeeInfoAndUpdateStructure extends ImportEmployeeInfo {

    public ImportEmployeeInfoAndUpdateStructure(final String username) {
	super(username);
    }

    @Override
    protected void updateInformation(final LocalDate now, final AccountabilityType accountabilityType, final module.organization.domain.Unit unit) {
	super.updateInformation(now, accountabilityType, unit);

	final MissionSystem missionSystem = MissionSystem.getInstance();
	final OrganizationalModel missionOrganizationalModel = missionSystem.getOrganizationalModel();
	final AccountabilityType organizationalMissionAccountabilityType = IstAccountabilityType.ORGANIZATIONAL_MISSIONS.readAccountabilityType();
	updateUnitInformation(missionOrganizationalModel, organizationalMissionAccountabilityType, unit);
    }

    private void updateUnitInformation(final OrganizationalModel missionOrganizationalModel, final AccountabilityType organizationalMissionAccountabilityType, final Unit unit) {
	if (!missionOrganizationalModel.containsUnit(unit)) {
	    final OrganizationalModel organizationalModel = findInstitutionalOrganizationalModel(missionOrganizationalModel);
	    if (organizationalModel != null && organizationalModel.containsUnit(unit)) {
		final AccountabilityType organizationalAccountabilityType = IstAccountabilityType.ORGANIZATIONAL.readAccountabilityType();
		updateStructure(unit, organizationalAccountabilityType, organizationalMissionAccountabilityType);
	    }
	}
	final Collection<Unit> parentUnits = unit.getParentUnits(organizationalMissionAccountabilityType);
	for (final Unit parentUnit : parentUnits) {
	    updateUnitInformation(missionOrganizationalModel, organizationalMissionAccountabilityType, parentUnit);
	}
    }

    private void updateStructure(final Party unit, final AccountabilityType organizationalAccountabilityType,
	    final AccountabilityType organizationalMissionAccountabilityType) {
	final Party organizationalParent = hasParent(unit, organizationalAccountabilityType);
	final Party missionParent = hasParent(unit, organizationalMissionAccountabilityType);
	if (organizationalParent != null && organizationalParent != missionParent) {
	    if (missionParent != null) {
		close(unit, organizationalMissionAccountabilityType);
	    }
	    organizationalParent.addChild(unit, organizationalMissionAccountabilityType, new LocalDate(), null);
	}
	if (!MissionSystem.getInstance().getOrganizationalModel().containsUnit(organizationalParent)) {
	    updateStructure(organizationalParent, organizationalAccountabilityType, organizationalMissionAccountabilityType);
	}
    }

    private void close(final Party unit, final AccountabilityType accountabilityType) {
	for (final Accountability accountability : unit.getParentAccountabilitiesSet()) {
	    if (accountability.isActiveNow() && accountability.getAccountabilityType() == accountabilityType) {
		accountability.setEndDate(new LocalDate().minusDays(1));
	    }
	}
    }

    private Party hasParent(final Party unit, final AccountabilityType accountabilityType) {
	for (final Accountability accountability : unit.getParentAccountabilitiesSet()) {
	    if (accountability.isActiveNow() && accountability.getAccountabilityType() == accountabilityType) {
		return accountability.getParent();
	    }
	}
	return null;
    }

    private OrganizationalModel findInstitutionalOrganizationalModel(final OrganizationalModel missionOrganizationalModel) {
	final Party firstParty = missionOrganizationalModel.getPartiesSet().iterator().next();
	for (final OrganizationalModel organizationalModel : MyOrg.getInstance().getOrganizationalModelsSet()) {
	    if (organizationalModel.getName().getContent().equals(firstParty.getPartyName().getContent())) {
		return organizationalModel;
	    }
	}
	return null;
    }

}
