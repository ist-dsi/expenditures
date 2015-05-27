package module.mission.domain;

import java.math.BigDecimal;

import module.finance.util.Money;

public class HalfPersonelExpenseItem extends HalfPersonelExpenseItem_Base {

    public HalfPersonelExpenseItem() {
        super();
    }

    @Override
    public Money getValue() {
        final DailyPersonelExpenseCategory dailyPersonelExpenseCategory = getDailyPersonelExpenseCategory();
        final double expenseFactor = getWeightedExpenseFactor();
        return dailyPersonelExpenseCategory.getValue().multiplyAndRound(new BigDecimal(expenseFactor));
    }

    @Override
    protected MissionItem createNewVersionInstance(final MissionVersion missionVersion) {
        return new HalfPersonelExpenseItem();
    }

}
