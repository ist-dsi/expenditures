/*
 * @(#)RefundProcessTotalValueStatistics.java
 *
 * Copyright 2009 Instituto Superior Tecnico
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

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

/**
 * 
 * @author Shezad Anavarali
 * 
 */
public class RefundProcessTotalValueStatistics implements Serializable {

    final Map<RefundProcessStateType, Money> result = new TreeMap<RefundProcessStateType, Money>();

    private int numberOfProcesses = 0;

    public RefundProcessTotalValueStatistics(final PaymentProcessYear acquisitionProcessYear) {
        for (RefundProcessStateType stateType : RefundProcessStateType.values()) {
            result.put(stateType, Money.ZERO);
        }

        for (final PaymentProcess paymentProcess : acquisitionProcessYear.getPaymentProcessSet()) {
            if (paymentProcess.isRefundProcess()) {
                numberOfProcesses++;
                final RefundProcessStateType refundProcessStateType =
                        ((RefundProcess) paymentProcess).getProcessState().getRefundProcessStateType();
                result.put(refundProcessStateType,
                        result.get(refundProcessStateType).add(paymentProcess.getRequest().getTotalValue()));
            }
        }
    }

    public static RefundProcessTotalValueStatistics create(final Integer year) {
        if (year != null) {
            final int y = year.intValue();
            for (final PaymentProcessYear paymentProcessYear : ExpenditureTrackingSystem.getInstance()
                    .getPaymentProcessYearsSet()) {
                if (paymentProcessYear.getYear().intValue() == y) {
                    return create(paymentProcessYear);
                }
            }
        }
        return null;
    }

    private static RefundProcessTotalValueStatistics create(final PaymentProcessYear paymentProcessYear) {
        return new RefundProcessTotalValueStatistics(paymentProcessYear);
    }

    public int getNumberOfProcesses() {
        return numberOfProcesses;
    }

    public Map<RefundProcessStateType, Money> getTotalValuesOfProcessesByRefundProcessStateType() {
        return result;
    }

}
