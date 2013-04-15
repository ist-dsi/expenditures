package module.mission.domain;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.LocalDate;

import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.fenixframework.Atomic;

public class DailyPersonelExpenseTable extends DailyPersonelExpenseTable_Base {

    public static final Comparator<DailyPersonelExpenseTable> COMPARATOR_BY_APLICABLE_SINCE =
            new Comparator<DailyPersonelExpenseTable>() {

                @Override
                public int compare(final DailyPersonelExpenseTable o1, final DailyPersonelExpenseTable o2) {
                    final LocalDate ld1 = o1.getAplicableSince();
                    final LocalDate ld2 = o2.getAplicableSince();
                    final int i = ld1.compareTo(ld2);
                    return i == 0 ? o2.hashCode() - o1.hashCode() : i;
                }

            };

    public DailyPersonelExpenseTable() {
        super();
        setMissionSystem(MissionSystem.getInstance());
    }

    public DailyPersonelExpenseTable(final LocalDate aplicableSince, final Class aplicableToMissionClass) {
        this();
        edit(aplicableSince, aplicableToMissionClass);
        final DailyPersonelExpenseTable previous = findDailyPersonelExpenseTableFor(aplicableSince.minusDays(1));
        if (previous != null) {
            for (final DailyPersonelExpenseCategory dailyPersonelExpenseCategory : previous
                    .getDailyPersonelExpenseCategoriesSet()) {
                createDailyPersonelExpenseCategory(dailyPersonelExpenseCategory.getDescription(),
                        dailyPersonelExpenseCategory.getValue(), dailyPersonelExpenseCategory.getMinSalaryValue());
            }
        }
    }

    public void edit(final LocalDate aplicableSince, final Class aplicableToMissionClass) {
        setAplicableSince(aplicableSince);
        setAplicableToMissionClass(aplicableToMissionClass);
        checkConsistent();
    }

    private void checkConsistent() {
        for (final DailyPersonelExpenseTable dailyPersonelExpenseTable : getMissionSystem().getDailyPersonelExpenseTablesSet()) {
            if (dailyPersonelExpenseTable != this
                    && dailyPersonelExpenseTable.getAplicableToMissionClass() == getAplicableToMissionClass()
                    && dailyPersonelExpenseTable.getAplicableSince().equals(getAplicableSince())) {
                throw new DomainException("error.duplicate.daily.personel.expense.table");
            }
        }
    }

    public Class getAplicableToMissionClass() {
        try {
            return Class.forName(getAplicableToMissionType());
        } catch (final ClassNotFoundException e) {
            throw new Error(e);
        }
    }

    public void setAplicableToMissionClass(Class aplicableToMissionClass) {
        setAplicableToMissionType(aplicableToMissionClass.getName());
    }

    public SortedSet<DailyPersonelExpenseTable> getDailyPersonelExpenseTablesForSameType() {
        final SortedSet<DailyPersonelExpenseTable> result = new TreeSet<DailyPersonelExpenseTable>(COMPARATOR_BY_APLICABLE_SINCE);
        for (final DailyPersonelExpenseTable dailyPersonelExpenseTable : getMissionSystem().getDailyPersonelExpenseTablesSet()) {
            if (dailyPersonelExpenseTable.getAplicableToMissionType().equals(getAplicableToMissionType())) {
                result.add(dailyPersonelExpenseTable);
            }
        }
        return result;
    }

    public DailyPersonelExpenseCategory createDailyPersonelExpenseCategory(final String description, final Money value,
            final BigDecimal minSalaryValue) {
        for (final DailyPersonelExpenseCategory dailyPersonelExpenseCategory : getDailyPersonelExpenseCategoriesSet()) {
            if (dailyPersonelExpenseCategory.getDescription().equals(description)) {
                throw new DomainException("error.duplicate.daily.personel.expense.category");
            }
        }
        return new DailyPersonelExpenseCategory(this, description, value, minSalaryValue);
    }

    public SortedSet<DailyPersonelExpenseCategory> getSortedDailyPersonelExpenseCategories() {
        final SortedSet<DailyPersonelExpenseCategory> result =
                new TreeSet<DailyPersonelExpenseCategory>(DailyPersonelExpenseCategory.COMPARATOR_BY_VALUE);
        result.addAll(getDailyPersonelExpenseCategoriesSet());
        return result;
    }

    @Atomic
    public void delete() {
        for (final DailyPersonelExpenseCategory dailyPersonelExpenseCategory : getDailyPersonelExpenseCategoriesSet()) {
            dailyPersonelExpenseCategory.delete();
        }
        removeMissionSystem();
        deleteDomainObject();
    }

    public DailyPersonelExpenseTable findDailyPersonelExpenseTableFor(final LocalDate localDate) {
        return findDailyPersonelExpenseTableFor(getAplicableToMissionType(), localDate);
    }

    public static DailyPersonelExpenseTable findDailyPersonelExpenseTableFor(final String aplicableToMissionType,
            final LocalDate localDate) {
        DailyPersonelExpenseTable result = null;
        for (final DailyPersonelExpenseTable dailyPersonelExpenseTable : MissionSystem.getInstance()
                .getDailyPersonelExpenseTablesSet()) {
            if (dailyPersonelExpenseTable.getAplicableToMissionType().equals(aplicableToMissionType)) {
                if (!dailyPersonelExpenseTable.getAplicableSince().isAfter(localDate)) {
                    if (result == null || dailyPersonelExpenseTable.getAplicableSince().isAfter(result.getAplicableSince())) {
                        result = dailyPersonelExpenseTable;
                    }
                }
            }
        }
        return result;
    }

    public static DailyPersonelExpenseTable findDailyPersonelExpenseTableFor(Class<? extends Mission> class1, LocalDate localDate) {
        return findDailyPersonelExpenseTableFor(class1.getName(), localDate);
    }

    public DailyPersonelExpenseCategory getMaxDailyPersonelExpenseCategory() {
        return Collections.min(getDailyPersonelExpenseCategoriesSet(), DailyPersonelExpenseCategory.COMPARATOR_BY_VALUE);
    }

    public DailyPersonelExpenseCategory getMinDailyPersonelExpenseCategory() {
        return Collections.max(getDailyPersonelExpenseCategoriesSet(), DailyPersonelExpenseCategory.COMPARATOR_BY_VALUE);
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        return getMissionSystem() == VirtualHost.getVirtualHostForThread().getMissionSystem();
    }

    @Deprecated
    public java.util.Set<module.mission.domain.DailyPersonelExpenseCategory> getDailyPersonelExpenseCategories() {
        return getDailyPersonelExpenseCategoriesSet();
    }

    @Deprecated
    public boolean hasAnyDailyPersonelExpenseCategories() {
        return !getDailyPersonelExpenseCategoriesSet().isEmpty();
    }

    @Deprecated
    public boolean hasAplicableSince() {
        return getAplicableSince() != null;
    }

    @Deprecated
    public boolean hasAplicableToMissionType() {
        return getAplicableToMissionType() != null;
    }

    @Deprecated
    public boolean hasMissionSystem() {
        return getMissionSystem() != null;
    }

}
