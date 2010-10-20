package module.mission.domain;

import java.math.BigDecimal;
import java.util.Set;

import module.organization.domain.Person;

public class Salary extends Salary_Base {
    
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
