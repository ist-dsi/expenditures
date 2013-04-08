/*
 * @(#)SimplifiedProcessTotalValueStatistics.java
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
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

/**
 * 
 * @author Shezad Anavarali
 * 
 */
public class SimplifiedProcessTotalValueStatistics implements Serializable {

    final Map<AcquisitionProcessStateType, Money> result = new TreeMap<AcquisitionProcessStateType, Money>();

    private int numberOfProcesses = 0;

    public SimplifiedProcessTotalValueStatistics(final PaymentProcessYear acquisitionProcessYear) {
        for (AcquisitionProcessStateType acquisitionProcessStateType : AcquisitionProcessStateType.values()) {
            result.put(acquisitionProcessStateType, Money.ZERO);
        }

        for (final PaymentProcess paymentProcess : acquisitionProcessYear.getPaymentProcessSet()) {
            if (paymentProcess.isSimplifiedProcedureProcess()) {
                numberOfProcesses++;
                final AcquisitionProcessStateType acquisitionProcessStateType =
                        ((SimplifiedProcedureProcess) paymentProcess).getAcquisitionProcessStateType();
                result.put(acquisitionProcessStateType,
                        result.get(acquisitionProcessStateType).add(paymentProcess.getRequest().getTotalValue()));
            }
        }
    }

    public static SimplifiedProcessTotalValueStatistics create(final Integer year) {
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

    private static SimplifiedProcessTotalValueStatistics create(final PaymentProcessYear paymentProcessYear) {
        return new SimplifiedProcessTotalValueStatistics(paymentProcessYear);
    }

    public int getNumberOfProcesses() {
        return numberOfProcesses;
    }

    public Map<AcquisitionProcessStateType, Money> getTotalValuesOfProcessesByAcquisitionProcessStateType() {
        return result;
    }

}
