package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.ActivityLog;
import module.workflow.domain.WorkflowLog;
import module.workflow.domain.WorkflowProcess;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;

public abstract class ProcessActivityLogStatistics {

    protected final List<LogEntry> logEntries = new ArrayList<LogEntry>();

    protected abstract void register(final PaymentProcessYear paymentProcessYear);

    protected void register(final PaymentProcess process) {
	final List<WorkflowLog> operationLogs = new ArrayList<WorkflowLog>(process.getExecutionLogs(ActivityLog.class));
	Collections.sort(operationLogs, WorkflowLog.COMPARATOR_BY_WHEN);

	final Set<LogEntry> logEntrySet = new HashSet<LogEntry>();

	for (int i = 1; i < operationLogs.size(); i++) {
	    final WorkflowLog operationLog = operationLogs.get(i);
	    final WorkflowLog previousLog = operationLogs.get(i - 1);

	    final long duration = calculateDuration(operationLog, previousLog);

	    final WorkflowActivity abstractActivity = getActivity(process, (ActivityLog) operationLog);
	    final LogEntry logEntry = getLogEntry(abstractActivity);
	    if (!logEntrySet.contains(logEntry)) {
		logEntrySet.add(logEntry);
		logEntry.countProcess();
	    }
	    logEntry.register(duration);
	}
    }

    protected WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> getActivity(PaymentProcess process,
	    ActivityLog operationLog) {
	return process.getActivity(operationLog.getOperation());
    }

    protected long calculateDuration(final WorkflowLog operationLog, final WorkflowLog previousLog) {
	final DateTime dateTime = operationLog.getWhenOperationWasRan();
	final DateTime previousDateTime = previousLog.getWhenOperationWasRan();
	return dateTime.getMillis() - previousDateTime.getMillis();
    }

    protected LogEntry getLogEntry(final WorkflowActivity abstractActivity) {
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
