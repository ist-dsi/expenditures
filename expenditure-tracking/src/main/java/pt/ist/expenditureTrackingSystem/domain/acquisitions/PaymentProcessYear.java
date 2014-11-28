/*
 * @(#)PaymentProcessYear.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class PaymentProcessYear extends PaymentProcessYear_Base {

    private PaymentProcessYear() {
        super();
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
        setCounter(Integer.valueOf(0));
    }

    private PaymentProcessYear(final Integer year) {
        this();
        setYear(year);
    }

    public Integer nextAcquisitionProcessYearNumber() {
        return getAndUpdateNextCountNumber();
    }

    private Integer getAndUpdateNextCountNumber() {
        setCounter(getCounter().intValue() + 1);
        return getCounter();
    }

    @Atomic
    public static PaymentProcessYear getPaymentProcessYearByYear(final Integer year) {
        PaymentProcessYear acquisitionProcessYear = findPaymentProcessYear(year);
        return acquisitionProcessYear != null ? acquisitionProcessYear : new PaymentProcessYear(year);
    }

    private static PaymentProcessYear findPaymentProcessYear(final Integer year) {
        for (PaymentProcessYear paymentProcessYear : ExpenditureTrackingSystem.getInstance().getPaymentProcessYears()) {
            if (paymentProcessYear.getYear().equals(year)) {
                return paymentProcessYear;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.SavedSearch> getYearSearches() {
        return getYearSearchesSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess> getPaymentProcess() {
        return getPaymentProcessSet();
    }

    @Deprecated
    public boolean hasAnyYearSearches() {
        return !getYearSearchesSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyPaymentProcess() {
        return !getPaymentProcessSet().isEmpty();
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

    @Deprecated
    public boolean hasCounter() {
        return getCounter() != null;
    }

    @Deprecated
    public boolean hasExpenditureTrackingSystem() {
        return getExpenditureTrackingSystem() != null;
    }

}
