package module.mission.domain;

import java.math.BigDecimal;

import myorg.domain.util.Money;

public class WithAccommodationPersonelExpenseItem extends WithAccommodationPersonelExpenseItem_Base {
    
    public WithAccommodationPersonelExpenseItem() {
        super();
    }

    @Override
    public double getWeightedExpenseFactor() {
	return getExpenseFactor() * 0.7;
    }

    @Override
    public Money getValue() {
	final DailyPersonelExpenseCategory dailyPersonelExpenseCategory = getDailyPersonelExpenseCategory();
	final double expenseFactor = getWeightedExpenseFactor();
	return dailyPersonelExpenseCategory.getValue().multiplyAndRound(new BigDecimal(expenseFactor));
    }

    @Override
    protected MissionItem createNewVersionInstance(final MissionVersion missionVersion) {
	return new WithAccommodationPersonelExpenseItem();
    }

}
