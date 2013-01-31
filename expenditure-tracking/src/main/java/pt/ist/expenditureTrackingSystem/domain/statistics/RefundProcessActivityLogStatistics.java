/*
 * @(#)RefundProcessActivityLogStatistics.java
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
import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class RefundProcessActivityLogStatistics extends ProcessActivityLogStatistics implements Serializable {

	public RefundProcessActivityLogStatistics(final RefundProcess refundProcess) {
		register(refundProcess);
	}

	public RefundProcessActivityLogStatistics(final PaymentProcessYear paymentProcessYear) {
		register(paymentProcessYear);
	}

	public static RefundProcessActivityLogStatistics create(final RefundProcess refundProcess) {
		return new RefundProcessActivityLogStatistics(refundProcess);
	}

	public static RefundProcessActivityLogStatistics create(final PaymentProcessYear paymentProcessYear) {
		return new RefundProcessActivityLogStatistics(paymentProcessYear);
	}

	public static RefundProcessActivityLogStatistics create(final Integer year) {
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

	public List<LogEntry> getLogEntries() {
		return logEntries;
	}

	protected void register(final PaymentProcessYear paymentProcessYear) {
		for (final PaymentProcess paymentProcess : paymentProcessYear.getPaymentProcessSet()) {
			if (paymentProcess.isRefundProcess()) {
				final RefundProcess refundProcess = (RefundProcess) paymentProcess;
				register(refundProcess);
			}
		}
	}

}
