package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.math.BigDecimal;

import module.workflow.activities.WorkflowActivity;

public class LogEntry {

    private final WorkflowActivity abstractActivity;

    private long duration = 0;

    private int numberOfProcesses = 0;

    public LogEntry(final WorkflowActivity abstractActivity) {
	this.abstractActivity = abstractActivity;
    }

    public void countProcess() {
	numberOfProcesses++;
    }

    public void register(final long duration) {
	this.duration += duration;
    }

    public WorkflowActivity getAbstractActivity() {
	return abstractActivity;
    }

    private static final BigDecimal DAYS_CONST = new BigDecimal(1000 * 3600 * 24);

    public BigDecimal getDays() {
	final BigDecimal bigDecimal = new BigDecimal(duration / numberOfProcesses);
	return bigDecimal.divide(DAYS_CONST, 100, BigDecimal.ROUND_HALF_UP);
    }

}
