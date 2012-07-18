package module.mission.domain;

import java.math.BigDecimal;

import pt.ist.bennu.core.domain.util.Money;

public class WithAccommodationAndTwoMealsPersonelExpenseItem extends WithAccommodationAndTwoMealsPersonelExpenseItem_Base {
    
    public WithAccommodationAndTwoMealsPersonelExpenseItem() {
        super();
    }

    @Override
    public double getWeightedExpenseFactor() {
	return getExpenseFactor() * 0.2;
    }

    @Override
    public Money getValue() {
	final DailyPersonelExpenseCategory dailyPersonelExpenseCategory = getDailyPersonelExpenseCategory();
	final double expenseFactor = getWeightedExpenseFactor();
	return dailyPersonelExpenseCategory.getValue().multiplyAndRound(new BigDecimal(expenseFactor));
    }

    @Override
    protected MissionItem createNewVersionInstance(final MissionVersion missionVersion) {
	return new WithAccommodationAndTwoMealsPersonelExpenseItem();
    }

}
