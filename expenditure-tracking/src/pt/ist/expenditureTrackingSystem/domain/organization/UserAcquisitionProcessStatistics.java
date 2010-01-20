package pt.ist.expenditureTrackingSystem.domain.organization;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.TreeMap;

import module.workflow.domain.WorkflowLog;
import myorg.domain.User;

import org.joda.time.Duration;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;

public class UserAcquisitionProcessStatistics implements Serializable {

    private final User user;

    private PaymentProcessYear paymentProcessYear;

    private int numberOfParticipatedProcesses;
    private int numberOfActivities;
    private Duration totalActivityDuration = new Duration(0);

    public class UserAcquisitionProcessTypeStatistics implements Serializable {

	private final String processType;

	private int numberOfParticipatedProcesses;
	private int numberOfActivities;
	private Duration totalActivityDuration = new Duration(0);

	private UserAcquisitionProcessTypeStatistics(final String processType) {
	    this.processType = processType;
	    processTypeMap.put(processType, this);
	}

	public int getNumberOfParticipatedProcesses() {
	    return numberOfParticipatedProcesses;
	}

	public int getNumberOfActivities() {
	    return numberOfActivities;
	}

	public PaymentProcessYear getPaymentProcessYear() {
	    return paymentProcessYear;
	}

	public Duration getAverateaActivityDuration() {
	    if (numberOfActivities == 0) {
		return totalActivityDuration;
	    }
	    final BigDecimal millis = new BigDecimal(totalActivityDuration.getMillis());
	    final BigDecimal divisor = new BigDecimal(numberOfActivities);
	    final BigDecimal result = millis.divide(divisor, RoundingMode.HALF_EVEN);
	    return new Duration(result.longValue());
	}

	private void registerProcessParticipation() {
	    numberOfParticipatedProcesses++;
	}

	private void registerActivity() {
	    numberOfActivities++;
	}

	private void registerActivity(final Duration duration) {
	    if (duration != null) {
		totalActivityDuration = totalActivityDuration.plus(duration);
	    }
	}

	public String getProcessType() {
	    return processType;
	}
    }

    private Map<String, UserAcquisitionProcessTypeStatistics> processTypeMap = new TreeMap<String, UserAcquisitionProcessTypeStatistics>();

    private UserAcquisitionProcessTypeStatistics getUserAcquisitionProcessTypeStatistics(final String processType) {
	return processTypeMap.containsKey(processType) ? processTypeMap.get(processType)
		: new UserAcquisitionProcessTypeStatistics(processType);
    }

    public UserAcquisitionProcessStatistics(final User user, final PaymentProcessYear paymentProcessYear) {
	if (user == null) {
	    throw new NullPointerException();
	}
	this.user = user;
	setPaymentProcessYear(paymentProcessYear);
    }

    public User getUser() {
        return user;
    }

    public int getNumberOfParticipatedProcesses() {
	return numberOfParticipatedProcesses;
    }

    public int getNumberOfActivities() {
        return numberOfActivities;
    }

    public PaymentProcessYear getPaymentProcessYear() {
        return paymentProcessYear;
    }

    public Duration getAverateaActivityDuration() {
	if (numberOfActivities == 0) {
	    return totalActivityDuration;
	}
	final BigDecimal millis = new BigDecimal(totalActivityDuration.getMillis());
	final BigDecimal divisor = new BigDecimal(numberOfActivities);
	final BigDecimal result = millis.divide(divisor, RoundingMode.HALF_EVEN);
	return new Duration(result.longValue());
    }

    public void setPaymentProcessYear(PaymentProcessYear paymentProcessYear) {
        this.paymentProcessYear = paymentProcessYear;

        processTypeMap.clear();

	int numberOfParticipatedProcesses = 0;
	int numberOfActivities = 0;
	if (paymentProcessYear != null) {
	    for (final PaymentProcess paymentProcess : paymentProcessYear.getPaymentProcessSet()) {
		if (participated(paymentProcess)) {
		    numberOfParticipatedProcesses++;

		    final String processType = getProcessType(paymentProcess);
		    UserAcquisitionProcessTypeStatistics userAcquisitionProcessTypeStatistics = getUserAcquisitionProcessTypeStatistics(processType);
		    userAcquisitionProcessTypeStatistics.registerProcessParticipation();

		    for (final WorkflowLog workflowLog : paymentProcess.getExecutionLogsSet()) {
			if (workflowLog.getActivityExecutor() == user) {
			    numberOfActivities++;
			    userAcquisitionProcessTypeStatistics.registerActivity();

			    final Duration duration = workflowLog.getDurationFromPreviousLog();
			    if (duration != null) {
				totalActivityDuration = totalActivityDuration.plus(duration);
				userAcquisitionProcessTypeStatistics.registerActivity(duration);
			    }
			}
		    }
		}
	    }
	}
	this.numberOfParticipatedProcesses = numberOfParticipatedProcesses;
	this.numberOfActivities = numberOfActivities;
    }

    private String getProcessType(final PaymentProcess paymentProcess) {
	return paymentProcess.getLocalizedName();
    }

    protected boolean participated(final PaymentProcess paymentProcess) {
	for (final WorkflowLog workflowLog : paymentProcess.getExecutionLogsSet()) {
	    if (workflowLog.getActivityExecutor() == user) {
		return true;
	    }
	}
	return false;
    }

    public Map<String, UserAcquisitionProcessTypeStatistics> getProcessTypeMap() {
        return processTypeMap;
    }

}
