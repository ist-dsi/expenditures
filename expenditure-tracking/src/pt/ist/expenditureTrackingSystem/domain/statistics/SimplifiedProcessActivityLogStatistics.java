package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.io.Serializable;
import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class SimplifiedProcessActivityLogStatistics extends ProcessActivityLogStatistics implements Serializable {

    public SimplifiedProcessActivityLogStatistics(final SimplifiedProcedureProcess simplifiedProcedureProcess) {
	register(simplifiedProcedureProcess);
    }

    public SimplifiedProcessActivityLogStatistics(final PaymentProcessYear paymentProcessYear) {
	register(paymentProcessYear);
    }

    public static SimplifiedProcessActivityLogStatistics create(final SimplifiedProcedureProcess simplifiedProcedureProcess) {
	return new SimplifiedProcessActivityLogStatistics(simplifiedProcedureProcess);
    }

    public static SimplifiedProcessActivityLogStatistics create(final PaymentProcessYear paymentProcessYear) {
	return new SimplifiedProcessActivityLogStatistics(paymentProcessYear);
    }

    public static SimplifiedProcessActivityLogStatistics create(final Integer year) {
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

}
