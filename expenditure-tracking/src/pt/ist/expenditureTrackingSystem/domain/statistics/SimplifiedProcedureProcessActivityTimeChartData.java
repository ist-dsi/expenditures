package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.ActivityLog;
import module.workflow.domain.WorkflowLog;
import module.workflow.domain.WorkflowProcess;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.util.Calculation.Operation;

public class SimplifiedProcedureProcessActivityTimeChartData extends PaymentProcessChartData<String> {

    private List<WorkflowActivity<? extends WorkflowProcess,? extends ActivityInformation>> activities = null;

    public SimplifiedProcedureProcessActivityTimeChartData(PaymentProcessYear paymentProcessYear) {
	super(paymentProcessYear, Operation.MEDIAN);
    }

    protected String getTitleKey() {
	return "label.activity.times.average";
    }

    protected String[] getCategories() {
	Set<WorkflowActivity<? extends WorkflowProcess,? extends ActivityInformation>> activities =
	    	new TreeSet<WorkflowActivity<? extends WorkflowProcess,? extends ActivityInformation>>(
	    		new Comparator<WorkflowActivity>() {

			    @Override
			    public int compare(WorkflowActivity o1, WorkflowActivity o2) {
				final int c = o1.getLocalizedName().compareTo(o2.getLocalizedName());
				return c == 0 ? o2.hashCode() - o1.hashCode() : c;
			    }
	});
	for (final PaymentProcessYear paymentProcessYear : ExpenditureTrackingSystem.getInstance().getPaymentProcessYearsSet()) {
	    for (final PaymentProcess paymentProcess : paymentProcessYear.getPaymentProcessSet()) {
		activities.addAll(paymentProcess.getActivities());
	    }
	}
	final String[] catagories = new String[activities.size()];
	int i = 0;
	for (final WorkflowActivity workflowActivity : activities) {
	    catagories[i++] = workflowActivity.getLocalizedName();
	}
	return catagories;
    }

    protected String getLabel(final String c) {
	return c;
    }

    @Override
    protected void registerData(final String key, final Number number) {
	final BigDecimal value = ((BigDecimal) number).divide(DAYS_CONST, 100, BigDecimal.ROUND_HALF_UP);
        super.registerData(key, value);
    }

    @Override
    protected void count(final PaymentProcess paymentProcess) {
	if (paymentProcess.isSimplifiedProcedureProcess()) {
	    final SimplifiedProcedureProcess simplifiedProcedureProcess = (SimplifiedProcedureProcess) paymentProcess;

	    final List<WorkflowLog> operationLogs = new ArrayList<WorkflowLog>(simplifiedProcedureProcess.getExecutionLogsSet());
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
			    final String key = abstractActivity.getLocalizedName();
			    calculation.registerValue(key, new BigDecimal(duration));
			}
		    }
		}
	    }
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
