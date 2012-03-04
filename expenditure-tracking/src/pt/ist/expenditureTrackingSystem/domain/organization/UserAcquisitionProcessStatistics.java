/*
 * @(#)UserAcquisitionProcessStatistics.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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

/**
 * 
 * @author Luis Cruz
 * 
 */
public class UserAcquisitionProcessStatistics implements Serializable {

    private final User user;

    private PaymentProcessYear paymentProcessYear;

    private int numberOfParticipatedProcesses = 0;
    private int numberOfActivities = 0;
    private long totalActivityDuration = 0;
    private int numberOfActivitiesForAverage = 0;

    public class UserAcquisitionProcessTypeStatistics implements Serializable {

	private final String processType;

	private int numberOfParticipatedProcesses;
	private int numberOfActivities;
	private long totalActivityDuration = 0;

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
		return new Duration(totalActivityDuration);
	    }
	    final BigDecimal millis = new BigDecimal(totalActivityDuration);
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

	private void registerActivity(final long duration) {
	    totalActivityDuration += duration;
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
	if (numberOfActivitiesForAverage == 0) {
	    return new Duration(totalActivityDuration);
	}
	final BigDecimal millis = new BigDecimal(totalActivityDuration);
	final BigDecimal divisor = new BigDecimal(numberOfActivitiesForAverage);
	final BigDecimal result = millis.divide(divisor, RoundingMode.HALF_EVEN);
	return new Duration(result.longValue());
    }

    public void setPaymentProcessYear(PaymentProcessYear paymentProcessYear) {
        this.paymentProcessYear = paymentProcessYear;

        processTypeMap.clear();
        numberOfParticipatedProcesses = 0;
        numberOfActivities = 0;
        totalActivityDuration = 0;
        numberOfActivitiesForAverage = 0;

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
				long millis = duration.getMillis();
				totalActivityDuration += millis;
				numberOfActivitiesForAverage++;
				userAcquisitionProcessTypeStatistics.registerActivity(millis);
			    }
			}
		    }
		}
	    }
	}
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
