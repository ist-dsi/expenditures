package module.mission.domain;

import java.math.BigDecimal;

import pt.ist.bennu.core.domain.util.Money;

public class WithAccommodationAndOneMealPersonelExpenseItem extends WithAccommodationAndOneMealPersonelExpenseItem_Base {

    public WithAccommodationAndOneMealPersonelExpenseItem() {
        super();
    }

    @Override
    public double getWeightedExpenseFactor() {
        return getExpenseFactor() * 0.4;
    }

    @Override
    public Money getValue() {
        final DailyPersonelExpenseCategory dailyPersonelExpenseCategory = getDailyPersonelExpenseCategory();
        final double expenseFactor = getWeightedExpenseFactor();
        return dailyPersonelExpenseCategory.getValue().multiplyAndRound(new BigDecimal(expenseFactor));
    }

    @Override
    protected MissionItem createNewVersionInstance(final MissionVersion missionVersion) {
        return new WithAccommodationAndOneMealPersonelExpenseItem();
    }

}
