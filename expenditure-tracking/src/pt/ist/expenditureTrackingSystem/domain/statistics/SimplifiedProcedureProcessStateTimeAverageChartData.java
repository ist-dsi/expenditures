package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import myorg.util.BundleUtil;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class SimplifiedProcedureProcessStateTimeAverageChartData extends PaymentProcessChartData {

    private long[] durations = null;
    private int[] counts = null;

    public SimplifiedProcedureProcessStateTimeAverageChartData(PaymentProcessYear paymentProcessYear) {
	super(paymentProcessYear);
	setTitle(BundleUtil.getStringFromResourceBundle("resources.AcquisitionResources", "label.process.state.times.average"));
    }

    private void init(final SimplifiedProcedureProcess simplifiedProcedureProcess) {
	if (durations == null) {
	    final int size = AcquisitionProcessStateType.values().length;
	    durations = durations = new long[size];
	    counts = counts= new int[size];
	}	
    }

    @Override
    public void calculateData() {
        super.calculateData();

        if (durations != null) {
            final BigDecimal DAYS_CONST = new BigDecimal(1000 * 3600 * 24);
            for (int i = 0; i < durations.length; i++) {
        	final int count = counts[i];
        	if (count > 0) {
        	    final long duration = durations[i];
        	    final BigDecimal average = new BigDecimal(duration).divide(new BigDecimal(count), RoundingMode.HALF_EVEN);
        	    final String key = AcquisitionProcessStateType.values()[i].getLocalizedName();
        	    final BigDecimal value = average.divide(DAYS_CONST, 100, BigDecimal.ROUND_HALF_UP);
        	    registerData(key, value);
        	}
            }
        }
    }

    @Override
    protected void count(final PaymentProcess paymentProcess) {
	if (paymentProcess.isSimplifiedProcedureProcess()) {
	    final SimplifiedProcedureProcess simplifiedProcedureProcess = (SimplifiedProcedureProcess) paymentProcess;
	    init(simplifiedProcedureProcess);

	    final List<ProcessState> processStates = new ArrayList<ProcessState>(simplifiedProcedureProcess.getProcessStatesSet());
	    Collections.sort(processStates, ProcessState.COMPARATOR_BY_WHEN);
	    for (int i = 1; i < processStates.size(); i++) {
		final AcquisitionProcessState processState = (AcquisitionProcessState) processStates.get(i);
		final AcquisitionProcessState previousState = (AcquisitionProcessState) processStates.get(i - 1);
		final DateTime stateChangeDateTime = processState.getWhenDateTime();
		final DateTime previousStateChangeDateTime = previousState.getWhenDateTime();
		final long startMillis = stateChangeDateTime.getMillis();
		final long duration = startMillis - previousStateChangeDateTime.getMillis();

		final AcquisitionProcessStateType acquisitionProcessStateType = previousState.getAcquisitionProcessStateType();
		final int index = acquisitionProcessStateType.ordinal();

		durations[index] += duration;
		counts[index]++;
	    }
	}
    }

}
