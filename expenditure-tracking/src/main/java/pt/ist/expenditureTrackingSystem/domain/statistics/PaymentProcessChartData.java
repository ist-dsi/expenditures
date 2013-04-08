/*
 * @(#)PaymentProcessChartData.java
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
package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.math.BigDecimal;
import java.util.Map.Entry;
import java.util.SortedMap;

import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.util.Calculation;
import pt.ist.expenditureTrackingSystem.util.Calculation.Operation;

/**
 * 
 * @author Luis Cruz
 * 
 */
public abstract class PaymentProcessChartData<C extends Comparable<C>> extends ChartData {

    protected final BigDecimal DAYS_CONST = new BigDecimal(1000 * 3600 * 24);

    protected final PaymentProcessYear paymentProcessYear;

    protected Calculation<C> calculation;

    private final Operation operation;

    public PaymentProcessChartData(final PaymentProcessYear paymentProcessYear, final Operation operation) {
        this.paymentProcessYear = paymentProcessYear;
        this.operation = operation;
        setTitle(BundleUtil.getStringFromResourceBundle("resources.AcquisitionResources", getTitleKey()));
        calculation = new Calculation<C>(getCategories(), operation);
    }

    public void calculateData() {
        for (final PaymentProcess paymentProcess : paymentProcessYear.getPaymentProcessSet()) {
            count(paymentProcess);
        }

        final SortedMap<C, BigDecimal> result = calculation.getResult(operation);
        for (Entry<C, BigDecimal> entry : result.entrySet()) {
            final String key = getLabel(entry.getKey());
            final BigDecimal value = entry.getValue();
            registerData(key, value);
        }
    }

    protected abstract String getTitleKey();

    protected abstract C[] getCategories();

    protected abstract String getLabel(final C c);

    protected abstract void count(final PaymentProcess paymentProcess);

    public SortedMap<C, BigDecimal> getResults(final Operation operation) {
        return calculation.getResult(operation);
    }

    public SortedMap<C, BigDecimal> getMinResults() {
        return calculation.getMins();
    }

    public SortedMap<C, BigDecimal> getMaxResults() {
        return calculation.getMaxs();
    }

}
