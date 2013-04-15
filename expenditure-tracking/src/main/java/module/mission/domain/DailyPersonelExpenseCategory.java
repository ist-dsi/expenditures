package module.mission.domain;

import java.math.BigDecimal;
import java.util.Comparator;

import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.fenixframework.Atomic;

public class DailyPersonelExpenseCategory extends DailyPersonelExpenseCategory_Base {

    public static final Comparator<DailyPersonelExpenseCategory> COMPARATOR_BY_VALUE =
            new Comparator<DailyPersonelExpenseCategory>() {

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

    public DailyPersonelExpenseCategory(final DailyPersonelExpenseTable dailyPersonelExpenseTable, final String description,
            final Money value, final BigDecimal minSalaryValue) {
        setDailyPersonelExpenseTable(dailyPersonelExpenseTable);
        setDescription(description);
        setValue(value);
        setMinSalaryValue(minSalaryValue);
    }

    @Atomic
    public void delete() {
        setDailyPersonelExpenseTable(null);
        setMissionSystem(null);
        deleteDomainObject();
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        return getMissionSystem() == VirtualHost.getVirtualHostForThread().getMissionSystem();
    }

    @Deprecated
    public java.util.Set<module.mission.domain.PersonelExpenseItem> getPersonelExpenseItems() {
        return getPersonelExpenseItemsSet();
    }

    @Deprecated
    public boolean hasAnyPersonelExpenseItems() {
        return !getPersonelExpenseItemsSet().isEmpty();
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

    @Deprecated
    public boolean hasMinSalaryValue() {
        return getMinSalaryValue() != null;
    }

    @Deprecated
    public boolean hasMissionSystem() {
        return getMissionSystem() != null;
    }

    @Deprecated
    public boolean hasDailyPersonelExpenseTable() {
        return getDailyPersonelExpenseTable() != null;
    }

}
