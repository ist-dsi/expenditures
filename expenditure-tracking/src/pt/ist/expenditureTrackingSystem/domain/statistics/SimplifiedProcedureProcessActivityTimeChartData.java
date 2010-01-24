package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.ActivityLog;
import module.workflow.domain.WorkflowLog;
import module.workflow.domain.WorkflowProcess;
import myorg.util.BundleUtil;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class SimplifiedProcedureProcessActivityTimeChartData extends PaymentProcessChartData {

    private List<Long>[] durations = null;
    //private long[] accumulatedTimes = null;
    //private int[] activityCount = null;
    private int[] order = null;

    private List<WorkflowActivity<? extends WorkflowProcess,? extends ActivityInformation>> activities = null;

    public SimplifiedProcedureProcessActivityTimeChartData(PaymentProcessYear paymentProcessYear) {
	super(paymentProcessYear);
	setTitle(BundleUtil.getStringFromResourceBundle("resources.AcquisitionResources", "label.activity.times.average"));
    }

    private void init(final SimplifiedProcedureProcess simplifiedProcedureProcess) {
	if (activities == null) {
	    activities = new ArrayList<WorkflowActivity<? extends WorkflowProcess,? extends ActivityInformation>>(simplifiedProcedureProcess.getActivities());
	    final int size = activities.size();
	    durations = new ArrayList[size];
//	    accumulatedTimes = new long[size];
//	    activityCount = new int[size];
	    order = new int[size];
	}	
    }

    private void sort() {
	for (int i = 0; i < order.length - 1; i++) {
	    final int minIndex = findMinValue(i);
	    swap(activities, i, minIndex);
//	    swap(accumulatedTimes, i, minIndex);
//	    swap(activityCount, i, minIndex);
	    swap(durations, i, minIndex);
	    swap(order, i, minIndex);
	}
    }

    private int findMinValue(final int i) {
	int min = i;
	for (int j = min + 1; j < order.length; j++) {
	    if (order[j] < order[min]) {
		min = j;
	    }
	}
	return min;
    }

    private void swap(final long[] values, final int i, final int j) {
	final long temp = values[i];
	values[i] = values[j];
	values[j] = temp;
    }

    private void swap(final int[] values, final int i, final int j) {
	final int temp = values[i];
	values[i] = values[j];
	values[j] = temp;
    }

    private void swap(final Object[] values, final int i, final int j) {
	final Object temp = values[i];
	values[i] = values[j];
	values[j] = temp;
    }

    private void swap(final List values, final int i, final int j) {
	final Object temp = values.set(i, values.get(j));
	values.set(j, temp);
    }

    @Override
    public void calculateData() {
        super.calculateData();

        if (activities != null) {
            sort();
            final BigDecimal DAYS_CONST = new BigDecimal(1000 * 3600 * 24);
            for (int i = 0; i < activities.size(); i++) {

        	final List<Long> activityDurations = durations[i];
        	final int size = activityDurations == null ? 0 : activityDurations.size();
        	if (size > 0) { 
        	    Collections.sort(activityDurations);

        	    final String key = activities.get(i).getLocalizedName();
        	    final double midPoint = 0.5 * size;
        	    final BigDecimal mediana;
        	    if (midPoint == (int) midPoint) {
        		final double nextPoint = midPoint - 1;
        		final Long value1 = activityDurations.get((int) midPoint);
        		final Long value2 = activityDurations.get((int) nextPoint);
        		final long sum = value1.longValue() + value2.longValue();
        		mediana = new BigDecimal(sum).divide(new BigDecimal(2));
        	    } else {
        		final double index = midPoint - 0.5;
        		mediana = new BigDecimal(activityDurations.get((int) index));
        	    }
//        	    final BigDecimal bigDecimal = new BigDecimal(accumulatedTimes[i] / count);
        	    final BigDecimal value = mediana.divide(DAYS_CONST, 100, BigDecimal.ROUND_HALF_UP);
        	    registerData(key, value);
        	}


//        	final int count = activityCount[i];
//        	if (count > 0) {
//        	    final String key = activities.get(i).getLocalizedName();
//        	    final BigDecimal bigDecimal = new BigDecimal(accumulatedTimes[i] / count);
//        	    final BigDecimal value = bigDecimal.divide(DAYS_CONST, 100, BigDecimal.ROUND_HALF_UP);
//        	    registerData(key, value);
//        	}
            }
        }
    }

    @Override
    protected void registerData(final String key, final Number number) {
	int indexOfSpan = key.indexOf('<');
	final String label = indexOfSpan > 0 ? key.substring(0, indexOfSpan) : key; 
        super.registerData(label, number);
    }

    @Override
    protected void count(final PaymentProcess paymentProcess) {
	if (paymentProcess.isSimplifiedProcedureProcess()) {
	    final SimplifiedProcedureProcess simplifiedProcedureProcess = (SimplifiedProcedureProcess) paymentProcess;
	    init(simplifiedProcedureProcess);

	    final List<WorkflowLog> operationLogs = new ArrayList<WorkflowLog>(simplifiedProcedureProcess.getExecutionLogsSet());
	    //final List<WorkflowLog> operationLogs = new ArrayList<WorkflowLog>(simplifiedProcedureProcess.getExecutionLogs(ActivityLog.class));
	    Collections.sort(operationLogs, WorkflowLog.COMPARATOR_BY_WHEN);

	    final List<ProcessState> processStates = new ArrayList<ProcessState>(simplifiedProcedureProcess.getProcessStatesSet());
	    Collections.sort(processStates, ProcessState.COMPARATOR_BY_WHEN);
	    for (int i = 1; i < processStates.size(); i++) {
		final AcquisitionProcessState processState = (AcquisitionProcessState) processStates.get(i);
		if (!processState.isCanceled()) { 
		    final AcquisitionProcessState previousState = (AcquisitionProcessState) processStates.get(i - 1);
		    final DateTime stateChangeDateTime = processState.getWhenDateTime();
		    final DateTime previousStateChangeDateTime = previousState.getWhenDateTime();

		    final long startMillis = stateChangeDateTime.getMillis();
		    final long duration = startMillis - previousStateChangeDateTime.getMillis();
		    final WorkflowLog workflowLog = findLastWorkflowLog(operationLogs, previousStateChangeDateTime, stateChangeDateTime);
		    if (workflowLog != null) {
			final WorkflowActivity abstractActivity = getActivity(simplifiedProcedureProcess, (ActivityLog) workflowLog);
			if (abstractActivity != null) {
			    final int activityIndex = getActivityIndex(abstractActivity);

			    if (durations[activityIndex] == null) {
				durations[activityIndex] = new ArrayList<Long>();
			    }
			    durations[activityIndex].add(Long.valueOf(duration));

//			    activityCount[activityIndex]++;
//			    accumulatedTimes[activityIndex] += duration;

			    order[activityIndex] = previousState.getAcquisitionProcessStateType().ordinal();
			}
		    }
		}
	    }

//	    for (int i = 1; i < operationLogs.size(); i++) {
//		final WorkflowLog operationLog = operationLogs.get(i);
//		final WorkflowLog previousLog = operationLogs.get(i - 1);
//
//		final long duration = calculateDuration(operationLog, previousLog);
//
//		final WorkflowActivity abstractActivity = getActivity(simplifiedProcedureProcess, (ActivityLog) operationLog);
//		if (abstractActivity != null) {
//		    final int activityIndex = getActivityIndex(abstractActivity);
//
//		    activityCount[activityIndex]++;
//		    accumulatedTimes[activityIndex] += duration;
//		}
//	    }

	}
    }

    private WorkflowLog findLastWorkflowLog(final List<WorkflowLog> orderedOperationLogs,
	    final DateTime start, final DateTime end) {
	WorkflowLog result = null;
	for (final WorkflowLog workflowLog : orderedOperationLogs) {
	    final DateTime whenOperationWasRan = workflowLog.getWhenOperationWasRan();
	    if (!whenOperationWasRan.isBefore(start) && !whenOperationWasRan.isAfter(end)) {
		if (workflowLog instanceof ActivityLog) {
		    result = workflowLog;
		}
	    }
	}
	return result;
    }

    protected int getActivityIndex(final WorkflowActivity workflowActivity) {
	for (int i = 0; i < activities.size(); i++) {
	    if (workflowActivity == activities.get(i)) {
		return i;
	    }
	}
	throw new Error("payment.process.does.not.contain.activity " + workflowActivity.getLocalizedName());
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

    protected long calculateDuration(final ProcessState processState, final ProcessState previousProcessState) {
	final DateTime dateTime = processState.getWhenDateTime();
	final DateTime previousDateTime = previousProcessState.getWhenDateTime();
	return dateTime.getMillis() - previousDateTime.getMillis();
    }

}
