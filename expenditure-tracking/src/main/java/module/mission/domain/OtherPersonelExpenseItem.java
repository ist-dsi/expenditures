/*
 * @(#)OtherPersonelExpenseItem.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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

import module.finance.util.Money;
import module.organization.domain.Person;

import org.joda.time.Days;
import org.joda.time.LocalDate;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class OtherPersonelExpenseItem extends OtherPersonelExpenseItem_Base {

    public OtherPersonelExpenseItem() {
        super();
    }

    @Override
    protected MissionItem createNewVersionInstance(final MissionVersion missionVersion) {
        return new OtherPersonelExpenseItem();
    }

    @Override
    protected void setNewVersionInformation(final MissionItem missionItem) {
        super.setNewVersionInformation(missionItem);
        final OtherPersonelExpenseItem otherPersonelExpenseItem = (OtherPersonelExpenseItem) missionItem;
        otherPersonelExpenseItem.setValue(getValue());
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

    @Override
    public boolean isConsistent() {
        return super.isConsistent() && doesNotExceedMaximumPossiblePersonelExpenseValue();
    }

    public boolean doesNotExceedMaximumPossiblePersonelExpenseValue() {
        final Money value = getValue();
        final MockPersonelExpenseItem mock =
                getMissionVersion().getMission().getAccommodationItems().isEmpty() ? new MockFullPersonelExpenseItem(this) : new MockWithAccommodationPersonelExpenseItem(
                        this);
        return value.isLessThanOrEqual(mock.getValue());
    }

    public static abstract class MockPersonelExpenseItem {

        final OtherPersonelExpenseItem missionItem;

        public MockPersonelExpenseItem(final OtherPersonelExpenseItem missionItem) {
            this.missionItem = missionItem;
        }

        public Money getValue() {
            return Money.ZERO;
        }

        public int calculateNumberOfDays() {
            final LocalDate startDate = missionItem.getStart().toLocalDate();
            final LocalDate endDate = missionItem.getEnd().toLocalDate().plusDays(1);
            return Days.daysBetween(startDate, endDate).getDays();
        }

        public double getWeightedExpenseFactor() {
            return getExpenseFactor();
        }

        public double getExpenseFactor() {
            double result = 0.0;
            for (final Person person : missionItem.getPeopleSet()) {
                result += getExpenseFactor(person);
            }
            return result;
        }

        public double getExpenseFactor(final Person person) {
            double result = 0.0;
            if (missionItem.getPeopleSet().contains(person)) {
                final Mission mission = missionItem.getMissionVersion().getMission();
                final PersonelExpenseItem firstPersonelExpenseItem = findFirstPersonelExpenseItemFor(mission, person);
                final PersonelExpenseItem lastPersonelExpenseItem = findLastPersonelExpenseItemFor(mission, person);
                int numberOfDays = calculateNumberOfDays();
                if (missionItem == firstPersonelExpenseItem) {
                    numberOfDays--;
                    result += mission.getFirstDayPersonelDayExpensePercentage(missionItem);
                }
                if (missionItem == lastPersonelExpenseItem && numberOfDays > 0) {
                    numberOfDays--;
                    result += mission.getLastDayPersonelDayExpensePercentage(missionItem);
                }
                result += (numberOfDays * mission.getMiddleDayPersonelDayExpensePercentage(firstPersonelExpenseItem));
            }
            return result;
        }

        private static PersonelExpenseItem findFirstPersonelExpenseItemFor(final Mission mission, final Person person) {
            PersonelExpenseItem result = null;
            for (final MissionItem missionItem : mission.getMissionItemsSet()) {
                if (missionItem.isPersonelExpenseItem()) {
                    final PersonelExpenseItem personelExpenseItem = (PersonelExpenseItem) missionItem;
                    if (personelExpenseItem.getPeopleSet().contains(person)) {
                        if (result == null || personelExpenseItem.getStart().isBefore(result.getStart())) {
                            result = personelExpenseItem;
                        }
                    }
                }
            }
            return result;
        }

        private static PersonelExpenseItem findLastPersonelExpenseItemFor(final Mission mission, final Person person) {
            PersonelExpenseItem result = null;
            for (final MissionItem missionItem : mission.getMissionItemsSet()) {
                if (missionItem.isPersonelExpenseItem()) {
                    final PersonelExpenseItem personelExpenseItem = (PersonelExpenseItem) missionItem;
                    if (personelExpenseItem.getPeopleSet().contains(person)) {
                        if (result == null || personelExpenseItem.getStart().isAfter(result.getStart())) {
                            result = personelExpenseItem;
                        }
                    }
                }
            }
            return result;
        }

    }

    public static class MockFullPersonelExpenseItem extends MockPersonelExpenseItem {

        public MockFullPersonelExpenseItem(final OtherPersonelExpenseItem missionItem) {
            super(missionItem);
        }

        @Override
        public Money getValue() {
            final DailyPersonelExpenseCategory dailyPersonelExpenseCategory = missionItem.getDailyPersonelExpenseCategory();
            final double expenseFactor = getWeightedExpenseFactor();
            return dailyPersonelExpenseCategory.getValue().multiplyAndRound(new BigDecimal(expenseFactor));
        }

    }

    public static class MockNoPersonelExpenseItem extends MockPersonelExpenseItem {

        public MockNoPersonelExpenseItem(final OtherPersonelExpenseItem missionItem) {
            super(missionItem);
        }

    }

    public static class MockWithAccommodationPersonelExpenseItem extends MockPersonelExpenseItem {

        public MockWithAccommodationPersonelExpenseItem(final OtherPersonelExpenseItem missionItem) {
            super(missionItem);
        }

        @Override
        public double getWeightedExpenseFactor() {
            return getExpenseFactor() * 0.7;
        }

        @Override
        public Money getValue() {
            final DailyPersonelExpenseCategory dailyPersonelExpenseCategory = missionItem.getDailyPersonelExpenseCategory();
            final double expenseFactor = getWeightedExpenseFactor();
            return dailyPersonelExpenseCategory.getValue().multiplyAndRound(new BigDecimal(expenseFactor));
        }

    }

    public class MockWithAccommodationAndOneMealPersonelExpenseItem extends MockWithAccommodationPersonelExpenseItem {

        public MockWithAccommodationAndOneMealPersonelExpenseItem(final OtherPersonelExpenseItem missionItem) {
            super(missionItem);
        }

        @Override
        public double getWeightedExpenseFactor() {
            return getExpenseFactor() * 0.4;
        }

        @Override
        public Money getValue() {
            final DailyPersonelExpenseCategory dailyPersonelExpenseCategory = missionItem.getDailyPersonelExpenseCategory();
            final double expenseFactor = getWeightedExpenseFactor();
            return dailyPersonelExpenseCategory.getValue().multiplyAndRound(new BigDecimal(expenseFactor));
        }

    }

    public class MockWithAccommodationAndTwoMealsPersonelExpenseItem extends MockWithAccommodationPersonelExpenseItem {

        public MockWithAccommodationAndTwoMealsPersonelExpenseItem(final OtherPersonelExpenseItem missionItem) {
            super(missionItem);
        }

        @Override
        public double getWeightedExpenseFactor() {
            return getExpenseFactor() * 0.2;
        }

        @Override
        public Money getValue() {
            final DailyPersonelExpenseCategory dailyPersonelExpenseCategory = missionItem.getDailyPersonelExpenseCategory();
            final double expenseFactor = getWeightedExpenseFactor();
            return dailyPersonelExpenseCategory.getValue().multiplyAndRound(new BigDecimal(expenseFactor));
        }

    }

}
