package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.io.Serializable;
import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

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
