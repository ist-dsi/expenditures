package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericLog;

public class SimplifiedProcessActivityLogStatistics implements Serializable {

    public static class LogEntry {

	private final AbstractActivity abstractActivity;

	private long duration = 0;

	private int numberOfProcesses = 0;

	public LogEntry(final AbstractActivity abstractActivity) {
	    this.abstractActivity = abstractActivity;
	}

	public void countProcess() {
	    numberOfProcesses++;
	}

	public void register(final long duration) {
	    this.duration += duration;
	}

	public AbstractActivity getAbstractActivity() {
	    return abstractActivity;
	}

	private static final BigDecimal DAYS_CONST = new BigDecimal(1000 * 3600 * 24);
	public BigDecimal getDays() {
	    final BigDecimal bigDecimal = new BigDecimal(duration / numberOfProcesses);
	    return bigDecimal.divide(DAYS_CONST, 100, BigDecimal.ROUND_HALF_UP);
	}

    }

    private final List<LogEntry> logEntries = new ArrayList<LogEntry>();

    public SimplifiedProcessActivityLogStatistics(final SimplifiedProcedureProcess simplifiedProcedureProcess) {
	register(simplifiedProcedureProcess);
    }

    public SimplifiedProcessActivityLogStatistics(final AcquisitionProcessYear acquisitionProcessYear) {
	register(acquisitionProcessYear);
    }

    private void register(final AcquisitionProcessYear acquisitionProcessYear) {
	for (final AcquisitionProcess acquisitionProcess : acquisitionProcessYear.getAcquisitionProcessesSet()) {
	    if (acquisitionProcess.isSimplifiedProcedureProcess()) {
		final SimplifiedProcedureProcess simplifiedProcedureProcess = (SimplifiedProcedureProcess) acquisitionProcess;
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
	    if (logEntry.abstractActivity == abstractActivity) {
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

    public static SimplifiedProcessActivityLogStatistics create(final AcquisitionProcessYear acquisitionProcessYear) {
	return new SimplifiedProcessActivityLogStatistics(acquisitionProcessYear);
    }

    public static SimplifiedProcessActivityLogStatistics create(final Integer year) {
	if (year != null) {
	    final int y = year.intValue();
	    for (final AcquisitionProcessYear acquisitionProcessYear : ExpenditureTrackingSystem.getInstance().getAcquisitionProcessYearsSet()) {
		if (acquisitionProcessYear.getYear().intValue() == y) {
		    return create(acquisitionProcessYear);
		}
	    }
	}
	return null;
    }

    public List<LogEntry> getLogEntries() {
        return logEntries;
    }

}
