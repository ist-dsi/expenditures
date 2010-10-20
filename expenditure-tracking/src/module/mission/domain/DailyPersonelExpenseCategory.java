package module.mission.domain;

import java.math.BigDecimal;
import java.util.Comparator;

import myorg.domain.util.Money;
import pt.ist.fenixWebFramework.services.Service;

public class DailyPersonelExpenseCategory extends DailyPersonelExpenseCategory_Base {

    public static final Comparator<DailyPersonelExpenseCategory> COMPARATOR_BY_VALUE = new Comparator<DailyPersonelExpenseCategory>() {

	@Override
	public int compare(final DailyPersonelExpenseCategory o1, final DailyPersonelExpenseCategory o2) {
	    final int i = o2.getValue().compareTo(o1.getValue());
	    return i == 0 ? o2.hashCode() - o1.hashCode() : i;
	}

    };

    public DailyPersonelExpenseCategory() {
        super();
        setMissionSystem(MissionSystem.getInstance());
    }

    public DailyPersonelExpenseCategory(final DailyPersonelExpenseTable dailyPersonelExpenseTable, final String description, final Money value, final BigDecimal minSalaryValue) {
	setDailyPersonelExpenseTable(dailyPersonelExpenseTable);
	setDescription(description);
	setValue(value);
	setMinSalaryValue(minSalaryValue);
    }

    @Service
    public void delete() {
	removeDailyPersonelExpenseTable();
	removeMissionSystem();
	deleteDomainObject();
    }
    
}
