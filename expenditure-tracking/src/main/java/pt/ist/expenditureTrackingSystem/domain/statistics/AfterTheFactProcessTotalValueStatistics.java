/*
 * @(#)AfterTheFactProcessTotalValueStatistics.java
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
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionType;

/**
 * 
 * @author Shezad Anavarali
 * 
 */
public class AfterTheFactProcessTotalValueStatistics implements Serializable {

    final Map<AfterTheFactAcquisitionType, Money> result = new TreeMap<AfterTheFactAcquisitionType, Money>();

    private int numberOfProcesses = 0;

    public AfterTheFactProcessTotalValueStatistics(final PaymentProcessYear acquisitionProcessYear) {
	for (AfterTheFactAcquisitionType stateType : AfterTheFactAcquisitionType.values()) {
	    result.put(stateType, Money.ZERO);
	}

	for (final PaymentProcess paymentProcess : acquisitionProcessYear.getPaymentProcessSet()) {

	    if (paymentProcess instanceof AfterTheFactAcquisitionProcess) {
		final AfterTheFactAcquisitionProcess process = (AfterTheFactAcquisitionProcess) paymentProcess;

		if (process.hasAcquisitionAfterTheFact() && !process.getAcquisitionAfterTheFact().getDeletedState()) {
		    numberOfProcesses++;

		    final AfterTheFactAcquisitionType afterTheFactAcquisitionType = process.getAcquisitionAfterTheFact()
			    .getAfterTheFactAcquisitionType();
		    result.put(afterTheFactAcquisitionType, result.get(afterTheFactAcquisitionType).add(
			    process.getAcquisitionAfterTheFact().getValue()));
		}
	    }
	}
    }

    public static AfterTheFactProcessTotalValueStatistics create(final Integer year) {
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

    private static AfterTheFactProcessTotalValueStatistics create(final PaymentProcessYear paymentProcessYear) {
	return new AfterTheFactProcessTotalValueStatistics(paymentProcessYear);
    }

    public int getNumberOfProcesses() {
	return numberOfProcesses;
    }

    public Map<AfterTheFactAcquisitionType, Money> getTotalValuesOfProcessesByAquisitionProcessStateType() {
	return result;
    }

}
