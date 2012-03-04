/*
 * @(#)FindMissionUnitsWithoutResponsible.java
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
package module.organizationIst.domain.task;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import module.organization.domain.AccountabilityType;
import module.organization.domain.OrganizationalModel;
import module.organization.domain.Party;
import module.organization.domain.Unit;
import myorg.domain.scheduler.ReadCustomTask;
import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class FindMissionUnitsWithoutResponsible extends ReadCustomTask {

    private class UnitsExpendituresResponsibles extends HashMap<Unit, HashSet<Person>> {
	private static final long serialVersionUID = 1L;

	public HashSet<Person> get(Unit unit) {
	    HashSet<Person> responsibles = super.get(unit);
	    if (responsibles == null) {
		responsibles = new HashSet<Person>();
		put(unit, responsibles);
	    }
	    return responsibles;
	}

	public void put(Unit unit, Person responsible) {
	    get(unit).add(responsible);
	}

	@Override
	public int size() {
	    int size = 0;
	    for (Unit unit : keySet()) {
		if (!get(unit).isEmpty()) {
		    ++size;
		}
	    }
	    return size;
	}
    }

    private static final String MISSIONS_ORGANIZATIONAL_MODEL_OID = "545460846594";
    private static final String MISSIONS_ACCOUNTABILITY_TYPE_OID = "403726926028";

    private static final String PERSONNEL_ACCOUNTABILITY_TYPE_OID = "403726926029";
    private static final String TEACHER_PERSONNEL_ACCOUNTABILITY_TYPE_OID = "403726926030";
    private static final String INVESTIGATOR_PERSONNEL_ACCOUNTABILITY_TYPE_OID = "403726926031";

    private static final String RESPONSIBLE_ACCOUNTABILITY_TYPE_OID = "403726925827";
    private static final String TEACHER_RESPONSIBLE_ACCOUNTABILITY_TYPE_OID = "403726926228";

    private static final String ACADEMIC_UNITS_PARTY_OID = "450971566087";

    private OrganizationalModel missionsOrganizationalModel;

    private final HashSet<AccountabilityType> personnelTypeList = new HashSet<AccountabilityType>();
    private final HashSet<AccountabilityType> responsibleTypeList = new HashSet<AccountabilityType>();
    private AccountabilityType missionAccType;

    private final HashSet<Unit> unitsWithResponsible = new HashSet<Unit>();
    private final HashSet<Unit> unitsWithoutResponsible = new HashSet<Unit>();

    private final HashSet<Unit> unitsWithExpendituresResponsible = new HashSet<Unit>();
    private final HashSet<Unit> unitsWithoutExpendituresResponsible = new HashSet<Unit>();

    private final UnitsExpendituresResponsibles unitsZeroAmountsExpendituresResponsibles = new UnitsExpendituresResponsibles();
    private final UnitsExpendituresResponsibles unitsNonZeroAmountsExpendituresResponsibles = new UnitsExpendituresResponsibles();

    private Unit academicUnitsUnit;

    @Override
    public void doIt() {

	init();

	for (Party party : missionsOrganizationalModel.getParties()) {
	    if (!Unit.class.isAssignableFrom(party.getClass())) {
		continue;
	    }

	    Unit unit = (Unit) party;
	    recursiveCheckUnit(unit);
	}

	printResults();
    }

    private void recursiveCheckUnit(Unit unit) {
	Collection<module.organization.domain.Person> personnel = unit.getChildPersons(personnelTypeList);
	Collection<module.organization.domain.Person> responsibles = unit.getChildPersons(responsibleTypeList);
	if ((!personnel.isEmpty()) && (responsibles.isEmpty())) {
	    unitsWithoutResponsible.add(unit);
	    checkExpenditureAuthorizations(unit);
	} else {
	    unitsWithResponsible.add(unit);
	}

	for (Unit descendentUnit : unit.getChildUnits(missionAccType)) {
	    recursiveCheckUnit(descendentUnit);
	}
    }

    private void checkExpenditureAuthorizations(Unit unit) {
	pt.ist.expenditureTrackingSystem.domain.organization.Unit expenditureUnit = unit.getExpenditureUnit();
	if (expenditureUnit == null) {
	    printUnitHasNoExpenditureUnit(unit);
	    unitsWithoutExpendituresResponsible.add(unit);
	    return;
	}

	for (Authorization authorization : expenditureUnit.getAuthorizations()) {
	    if (authorization.getPerson() == null) {
		printAuthorizationHasNoPerson(authorization);
	    }
	    if (authorization.getMaxAmount().equals(new Money("0"))) {
		unitsZeroAmountsExpendituresResponsibles.put(unit, authorization.getPerson());
	    } else {
		unitsNonZeroAmountsExpendituresResponsibles.put(unit, authorization.getPerson());
	    }
	}

	if (unitsZeroAmountsExpendituresResponsibles.get(unit).isEmpty()
		&& unitsNonZeroAmountsExpendituresResponsibles.get(unit).isEmpty()) {
	    unitsWithoutExpendituresResponsible.add(unit);
	} else {
	    unitsWithExpendituresResponsible.add(unit);
	}
    }

    private void printUnitHasNoExpenditureUnit(Unit unit) {
	out.println("WARNING!");
	printUnit(unit);
	out.println("has no expenditure unit.");
	out.println();
    }

    private void printAuthorizationHasNoPerson(Authorization authorization) {
	out.println("WARNING!");
	out.println("Authorization: " + authorization.getJustification() + " [ " + authorization.getExternalId() + " ] ");
	out.println("has no associated person.");
	out.println();
    }

    private void init() {
	missionsOrganizationalModel = AbstractDomainObject.fromExternalId(MISSIONS_ORGANIZATIONAL_MODEL_OID);

	missionAccType = AbstractDomainObject.fromExternalId(MISSIONS_ACCOUNTABILITY_TYPE_OID);

	AccountabilityType personnelType = AbstractDomainObject.fromExternalId(PERSONNEL_ACCOUNTABILITY_TYPE_OID);
	AccountabilityType teacherPersonnelType = AbstractDomainObject.fromExternalId(TEACHER_PERSONNEL_ACCOUNTABILITY_TYPE_OID);
	AccountabilityType investigatorPersonnelType = AbstractDomainObject
		.fromExternalId(INVESTIGATOR_PERSONNEL_ACCOUNTABILITY_TYPE_OID);
	personnelTypeList.add(personnelType);
	personnelTypeList.add(teacherPersonnelType);
	personnelTypeList.add(investigatorPersonnelType);

	AccountabilityType responsibleType = AbstractDomainObject.fromExternalId(RESPONSIBLE_ACCOUNTABILITY_TYPE_OID);
	AccountabilityType teacherResponsibleType = AbstractDomainObject
		.fromExternalId(TEACHER_RESPONSIBLE_ACCOUNTABILITY_TYPE_OID);
	responsibleTypeList.add(responsibleType);
	responsibleTypeList.add(teacherResponsibleType);

	academicUnitsUnit = AbstractDomainObject.fromExternalId(ACADEMIC_UNITS_PARTY_OID);
    }

    private void printResults() {
	out.println("Found " + unitsWithoutResponsible.size() + " cases without responsible, in a total of "
		+ (unitsWithoutResponsible.size() + unitsWithResponsible.size()));

	out.println("Of the " + unitsWithoutResponsible.size() + " cases without responsible "
		+ unitsWithExpendituresResponsible.size() + " have expenditures responsibles, and "
		+ unitsWithoutExpendituresResponsible.size() + " have not.");

	out.println();
	out.println();
	out.println();
	out.println();
	out.println();

	out.println("The following " + unitsWithoutExpendituresResponsible.size() + " units have no expenditures responsible: ");
	out.println();
	printUnitsWithoutExpendituresResponsible();

	out.println();
	out.println();
	out.println();
	out.println();
	out.println();

	out.println("The following " + unitsNonZeroAmountsExpendituresResponsibles.size()
		+ " units have at least one Non-Zero-Amount expenditures responsible: ");
	printUnitsWithExpendituresNonZeroAmountResponsibles();

	out.println();
	out.println();
	out.println();
	out.println();
	out.println();

	out.println("The following "
		+ (unitsWithExpendituresResponsible.size() - unitsNonZeroAmountsExpendituresResponsibles.size())
		+ " units only have Zero-Amount expenditures responsible: ");
	printUnitsWithExpendituresZeroAmountResponsibles();
    }

    private void printUnitsWithoutExpendituresResponsible() {
	for (Unit unit : unitsWithoutExpendituresResponsible) {
	    printUnit(unit);
	    recursiveCheckParentsForAcademicUnits(unit.getParentUnits(missionAccType));
	}
    }

    private void recursiveCheckParentsForAcademicUnits(Collection<Unit> parentUnits) {
	if (parentUnits.contains(academicUnitsUnit)) {
	    out.println(" - WARNING: This unit is an academic unit!");
	} else {
	    for (Unit parentUnit : parentUnits) {
		recursiveCheckParentsForAcademicUnits(parentUnit.getParentUnits(missionAccType));
	    }
	}
    }

    private void printUnitsWithExpendituresNonZeroAmountResponsibles() {
	for (Unit unit : unitsWithExpendituresResponsible) {
	    if (unitsNonZeroAmountsExpendituresResponsibles.get(unit).size() == 0) {
		continue;
	    }
	    out.println();
	    printUnit(unit);
	    out.println(unitsNonZeroAmountsExpendituresResponsibles.get(unit).size()
		    + " Non-Zero-Amount Expenditures Responsibles:");
	    for (Person person : unitsNonZeroAmountsExpendituresResponsibles.get(unit)) {
		printExpendituresResponsible(person);
	    }

	    if (unitsZeroAmountsExpendituresResponsibles.get(unit).size() == 0) {
		continue;
	    }
	    out.println(unitsZeroAmountsExpendituresResponsibles.get(unit).size() + " Zero-Amount Expenditures Responsibles:");
	    for (Person person : unitsZeroAmountsExpendituresResponsibles.get(unit)) {
		printExpendituresResponsible(person);
	    }
	}
    }

    private void printUnitsWithExpendituresZeroAmountResponsibles() {
	for (Unit unit : unitsWithExpendituresResponsible) {
	    if (unitsNonZeroAmountsExpendituresResponsibles.get(unit).size() != 0) {
		continue;
	    }
	    out.println();
	    printUnit(unit);
	    out.println(unitsZeroAmountsExpendituresResponsibles.get(unit).size() + " Zero-Amount Expenditures Responsibles:");
	    for (Person person : unitsZeroAmountsExpendituresResponsibles.get(unit)) {
		printExpendituresResponsible(person);
	    }
	}
    }

    private void printExpendituresResponsible(Person person) {
	out.println("Person: " + person.getName() + " [ " + person.getExternalId() + " ] ");
    }

    private void printUnit(Unit unit) {
	out.println("Unit: " + unit.getPresentationName() + " [ " + unit.getExternalId() + " ] ");
    }
}
