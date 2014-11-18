package module.mission.domain;

import java.math.BigDecimal;

import module.finance.util.Money;
import module.organization.domain.Person;

public class FullPersonelExpenseItem extends FullPersonelExpenseItem_Base {

    public FullPersonelExpenseItem() {
        super();
    }

    @Override
    public Money getValue() {
        final DailyPersonelExpenseCategory dailyPersonelExpenseCategory = getDailyPersonelExpenseCategory();
        final double expenseFactor = getWeightedExpenseFactor();
        return dailyPersonelExpenseCategory.getValue().multiplyAndRound(new BigDecimal(expenseFactor));
    }

    public static int calculateNumberOfFullPersonelExpenseDays(final Mission mission, final Person person) {
        int result = 0;
        for (final MissionItem missionItem : mission.getMissionItemsSet()) {
            if (missionItem instanceof FullPersonelExpenseItem && missionItem.getPeopleSet().contains(person)) {
                final FullPersonelExpenseItem fullPersonelExpenseItem = (FullPersonelExpenseItem) missionItem;
                result += fullPersonelExpenseItem.calculateNumberOfDays();
            }
        }
        return result;
    }

    @Override
    protected MissionItem createNewVersionInstance(final MissionVersion missionVersion) {
        return new FullPersonelExpenseItem();
    }

}
