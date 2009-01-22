package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;

public class LogEntry {

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
