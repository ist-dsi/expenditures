/*
 * @(#)Salary.java
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
package module.mission.domain;

import java.math.BigDecimal;
import java.util.Set;

import module.organization.domain.Party;
import module.organization.domain.Person;
import pt.ist.bennu.core.domain.MyOrg;
import dml.runtime.RelationAdapter;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class Salary extends Salary_Base {

    public static class SalaryPartyMyOrgListener extends RelationAdapter<Party, MyOrg> {

	@Override
	public void afterRemove(final Party o1, final MyOrg o2) {
	    if (o1.isPerson()) {
		final Person person = (Person) o1;
		if (person.hasSalary()) {
		    person.removeSalary();
		}
	    }
	    super.afterRemove(o1, o2);
	}

    }

    static {
	Party.MyOrgParty.addListener(new SalaryPartyMyOrgListener());
    }

    public Salary() {
        super();
        setMissionSystem(MissionSystem.getInstance());
    }

    public Salary(final BigDecimal value) {
	this();
	setValue(value);
    }

    public static void setSalary(final Person person, final BigDecimal value, boolean isGovernmentMember) {
	if (person != null) {
	    if (value != null) {
		final Salary salary = findOrCreate(value);
		person.setSalary(salary);
	    }
	    final Set<Person> governmentMembers = MissionSystem.getInstance().getGovernmentMembersSet();
	    if (isGovernmentMember) {
		governmentMembers.add(person);
	    } else {
		governmentMembers.remove(person);
	    }
	}
    }

    private static Salary findOrCreate(final BigDecimal value) {
	for (final Salary salary : MissionSystem.getInstance().getSalariesSet()) {
	    if (salary.getValue().equals(value)) {
		return salary;
	    }
	}
	return new Salary(value);
    }

    public static DailyPersonelExpenseCategory getDefaultDailyPersonelExpenseCategory(
	    final DailyPersonelExpenseTable dailyPersonelExpenseTable, final Person person) {
	if (person.getMissionSystemFromGovernmentMembership() != null) {
	    return dailyPersonelExpenseTable.getMaxDailyPersonelExpenseCategory();
	}
	final BigDecimal salary = person.hasSalary() ? person.getSalary().getValue() : new BigDecimal(0);
	DailyPersonelExpenseCategory result = null;
	for (final DailyPersonelExpenseCategory category : dailyPersonelExpenseTable.getDailyPersonelExpenseCategoriesSet()) {
	    if (category.getMinSalaryValue().compareTo(salary) <= 0) {
		if (result == null || category.getMinSalaryValue().compareTo(result.getMinSalaryValue()) > 0) {
		    result = category;
		}
	    }
	}
	return result == null ? dailyPersonelExpenseTable.getMinDailyPersonelExpenseCategory() : result;
    }

    public static DailyPersonelExpenseCategory getDefaultDailyPersonelExpenseCategory(
	    final DailyPersonelExpenseTable dailyPersonelExpenseTable, final Set<Person> people) {
	if (contiansGovernmentMembers(people)) {
	    return dailyPersonelExpenseTable.getMaxDailyPersonelExpenseCategory();
	}
	final BigDecimal salary = maxSalary(people);
	DailyPersonelExpenseCategory result = null;
	for (final DailyPersonelExpenseCategory category : dailyPersonelExpenseTable.getDailyPersonelExpenseCategoriesSet()) {
	    if (category.getMinSalaryValue().compareTo(salary) <= 0) {
		if (result == null || category.getMinSalaryValue().compareTo(result.getMinSalaryValue()) > 0) {
		    result = category;
		}
	    }
	}
	return result;
    }

    private static boolean contiansGovernmentMembers(final Set<Person> people) {
	for (final Person person : people) {
	    if (person.getMissionSystemFromGovernmentMembership() != null) {
		return true;
	    }
	}
	return false;
    }

    private static BigDecimal maxSalary(final Set<Person> people) {
	BigDecimal result = new BigDecimal(0);
	for (final Person person : people) {
	    final Salary salary = person.getSalary();
	    if (salary != null) {
		final BigDecimal value = salary.getValue();
		if (salary != null && (result == null || value.compareTo(result) > 0)) {
		    result = value;
		}
	    }
	}
	return result;
    }

}
