package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import module.workflow.domain.ActivityLog;
import module.workflow.domain.WorkflowLog;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;

public abstract class ProcessActivityLogStatistics {

    protected final List<LogEntry> logEntries = new ArrayList<LogEntry>();

    protected void register(final PaymentProcessYear paymentProcessYear) {
	for (final PaymentProcess paymentProcess : paymentProcessYear.getPaymentProcessSet()) {
	    if (paymentProcess.isRefundProcess()) {
		final RefundProcess refundProcess = (RefundProcess) paymentProcess;
		register(refundProcess);
	    }
	}
    }

    protected void register(final PaymentProcess process) {
	final List<WorkflowLog> operationLogs = new ArrayList<WorkflowLog>(process.getExecutionLogsSet());
	Collections.sort(operationLogs, WorkflowLog.COMPARATOR_BY_WHEN);

	final Set<LogEntry> logEntrySet = new HashSet<LogEntry>();

	for (int i = 1; i < operationLogs.size(); i++) {
	    final WorkflowLog operationLog = operationLogs.get(i);
	    final WorkflowLog previousLog = operationLogs.get(i - 1);

	    final long duration = calculateDuration(operationLog, previousLog);

	    final AbstractActivity abstractActivity = getActivity(process, operationLog);
	    final LogEntry logEntry = getLogEntry(abstractActivity);
	    if (!logEntrySet.contains(logEntry)) {
		logEntrySet.add(logEntry);
		logEntry.countProcess();
	    }
	    logEntry.register(duration);
	}
    }

    protected AbstractActivity getActivity(PaymentProcess process, WorkflowLog operationLog) {

	String operationName = ((ActivityLog) operationLog).getOperation();
	int index = operationName.lastIndexOf('.');
	return process.getActivityByName(operationName.substring(index + 1));
    }

    protected long calculateDuration(final WorkflowLog operationLog, final WorkflowLog previousLog) {
	final DateTime dateTime = operationLog.getWhenOperationWasRan();
	final DateTime previousDateTime = previousLog.getWhenOperationWasRan();
	return dateTime.getMillis() - previousDateTime.getMillis();
    }

    protected LogEntry getLogEntry(final AbstractActivity abstractActivity) {
	for (final LogEntry logEntry : logEntries) {
	    if (logEntry.getAbstractActivity() == abstractActivity) {
		return logEntry;
	    }
	}
	final LogEntry logEntry = new LogEntry(abstractActivity);
	logEntries.add(logEntry);
	return logEntry;
    }

}
