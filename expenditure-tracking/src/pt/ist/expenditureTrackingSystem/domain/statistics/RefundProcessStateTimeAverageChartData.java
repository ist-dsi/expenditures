package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.util.Calculation.Operation;

public class RefundProcessStateTimeAverageChartData extends RefundProcessStateTypeChartData {

    public RefundProcessStateTimeAverageChartData(final PaymentProcessYear paymentProcessYear) {
	super(paymentProcessYear, Operation.AVERAGE);
    }

    @Override
    protected String getTitleKey() {
	return "label.process.state.times.average";
    }

    @Override
    protected void registerData(final String key, final Number number) {
	final BigDecimal value = ((BigDecimal) number).divide(DAYS_CONST, 100, BigDecimal.ROUND_HALF_UP);
        super.registerData(key, value);
    }

    @Override
    protected void count(final PaymentProcess paymentProcess) {
	if (paymentProcess.isRefundProcess()) {
	    final RefundProcess refundProcess = (RefundProcess) paymentProcess;

	    final List<ProcessState> processStates = new ArrayList<ProcessState>(refundProcess.getProcessStatesSet());
	    Collections.sort(processStates, ProcessState.COMPARATOR_BY_WHEN);
	    for (int i = 1; i < processStates.size(); i++) {
		final RefundProcessState processState = (RefundProcessState) processStates.get(i);
		final RefundProcessState previousState = (RefundProcessState) processStates.get(i - 1);
		final DateTime stateChangeDateTime = processState.getWhenDateTime();
		final DateTime previousStateChangeDateTime = previousState.getWhenDateTime();
		final long startMillis = stateChangeDateTime.getMillis();
		final long duration = startMillis - previousStateChangeDateTime.getMillis();

		final RefundProcessStateType refundProcessStateType = previousState.getRefundProcessStateType();
		calculation.registerValue(refundProcessStateType, new BigDecimal(duration));
	    }
	}
    }

}
