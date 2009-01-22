package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericLog;

public class SimplifiedProcessActivityLogStatistics implements Serializable {

    private final List<LogEntry> logEntries = new ArrayList<LogEntry>();

    public SimplifiedProcessActivityLogStatistics(final SimplifiedProcedureProcess simplifiedProcedureProcess) {
	register(simplifiedProcedureProcess);
    }

    public SimplifiedProcessActivityLogStatistics(final PaymentProcessYear paymentProcessYear) {
	register(paymentProcessYear);
    }

    private void register(final PaymentProcessYear paymentProcessYear) {
	for (final PaymentProcess paymentProcess : paymentProcessYear.getPaymentProcessSet()) {
	    if (paymentProcess.isSimplifiedProcedureProcess()) {
		final SimplifiedProcedureProcess simplifiedProcedureProcess = (SimplifiedProcedureProcess) paymentProcess;
		register(simplifiedProcedureProcess);
	    }
	}
    }

    private void register(final SimplifiedProcedureProcess simplifiedProcedureProcess) {
	final List<GenericLog> operationLogs = new ArrayList<GenericLog>(simplifiedProcedureProcess.getExecutionLogsSet());
	Collections.sort(operationLogs, GenericLog.COMPARATOR_BY_WHEN);

	final Set<LogEntry> logEntrySet = new HashSet<LogEntry>();

	for (int i = 1; i < operationLogs.size(); i++) {
	    final GenericLog operationLog = operationLogs.get(i);
	    final GenericLog previousLog = operationLogs.get(i - 1);

	    final long duration = calculateDuration(operationLog, previousLog);

	    final AbstractActivity abstractActivity = operationLog.getActivity();
	    final LogEntry logEntry = getLogEntry(abstractActivity);
	    if (!logEntrySet.contains(logEntry)) {
		logEntrySet.add(logEntry);
		logEntry.countProcess();
	    }
	    logEntry.register(duration);
	}
    }

    private long calculateDuration(final GenericLog operationLog, final GenericLog previousLog) {
	final DateTime dateTime = operationLog.getWhenOperationWasRan();
	final DateTime previousDateTime = previousLog.getWhenOperationWasRan();
	return dateTime.getMillis() - previousDateTime.getMillis();
    }

    private LogEntry getLogEntry(final AbstractActivity abstractActivity) {
	for (final LogEntry logEntry : logEntries) {
	    if (logEntry.getAbstractActivity() == abstractActivity) {
		return logEntry;
	    }
	}
	final LogEntry logEntry = new LogEntry(abstractActivity);
	logEntries.add(logEntry);
	return logEntry;
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
	    for (final PaymentProcessYear paymentProcessYear : ExpenditureTrackingSystem.getInstance().getPaymentProcessYearsSet()) {
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
