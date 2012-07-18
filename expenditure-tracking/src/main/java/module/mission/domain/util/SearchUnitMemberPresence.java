/*
 * @(#)SearchUnitMemberPresence.java
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;
import module.organization.domain.Accountability;
import module.organization.domain.AccountabilityType;
import module.organization.domain.OrganizationalModel;
import module.organization.domain.Party;
import module.organization.domain.Person;
import module.organization.domain.Unit;

import org.joda.time.LocalDate;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class SearchUnitMemberPresence implements Serializable {

    private Unit unit;
    private LocalDate day = new LocalDate();
    private List<AccountabilityType> accountabilityTypes = new ArrayList<AccountabilityType>();
    private boolean includeSubUnits = true;
    private boolean onMission = true;

    public SearchUnitMemberPresence(final Unit unit) {
	this.unit = unit;
	accountabilityTypes.addAll(MissionSystem.getInstance().getAccountabilityTypesRequireingAuthorization());
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(final LocalDate day) {
        this.day = day;
    }

    public List<AccountabilityType> getAccountabilityTypes() {
        return accountabilityTypes;
    }

    public void setAccountabilityTypes(final List<AccountabilityType> accountabilityTypes) {
        this.accountabilityTypes = accountabilityTypes;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(final Unit unit) {
        this.unit = unit;
    }

    public boolean isIncludeSubUnits() {
        return includeSubUnits;
    }

    public void setIncludeSubUnits(final boolean includeSubUnits) {
        this.includeSubUnits = includeSubUnits;
    }

    public boolean isOnMission() {
        return onMission;
    }

    public void setOnMission(boolean onMission) {
        this.onMission = onMission;
    }

    public Set<Person> search() {
	final Set<Person> result = new TreeSet<Person>(Person.COMPARATOR_BY_NAME);
	search(result, unit);
	return result;
    }

    private void search(final Set<Person> result, final Unit unit) {
	final OrganizationalModel organizationalModel = MissionSystem.getInstance().getOrganizationalModel();
	final Set<AccountabilityType> accountabilityTypesSet = organizationalModel.getAccountabilityTypesSet();
	for (final Accountability accountability : unit.getChildAccountabilitiesSet()) {
	    final AccountabilityType accountabilityType = accountability.getAccountabilityType();
	    if (accountabilityTypesSet.contains(accountabilityType) && accountability.isActiveNow()) {
		final Party child = accountability.getChild();
		if (child.isUnit()) {
		    if (includeSubUnits) {
			search(result, (Unit) child);
		    }
		} else {
		    if (accountabilityTypes.contains(accountabilityType)) {
			final Person person = (Person) child;
			final boolean hasMission = hasMissionOnDay(person);
			//if (onMission && hasMission || !onMission && !hasMission) {
			if (!(onMission ^ hasMission)) {
			    result.add(person);
			}
		    }
		}
	    }
	}
    }

    private boolean hasMissionOnDay(final Person person) {
	for (final Mission mission : person.getMissionsSet()) {
	    final LocalDate departure = mission.getDaparture().toLocalDate();
	    final LocalDate arrival = mission.getArrival().toLocalDate();
	    if (!day.isBefore(departure) && !day.isAfter(arrival)) {
		final MissionProcess missionProcess = mission.getMissionProcess();
		if (!missionProcess.isCanceled() && !missionProcess.isUnderConstruction()) {
		    return true;
		}
	    }
	}
	return false;
    }

}
