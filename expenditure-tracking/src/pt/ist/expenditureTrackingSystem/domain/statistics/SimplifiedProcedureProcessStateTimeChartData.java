package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.util.SortedMap;
import java.util.Map.Entry;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.util.AcquisitionProcessStateTypeCounter;

public class SimplifiedProcedureProcessStateTimeChartData extends PaymentProcessChartData {

    protected final AcquisitionProcessStateTypeCounter processCounter = new AcquisitionProcessStateTypeCounter();

    public SimplifiedProcedureProcessStateTimeChartData(PaymentProcessYear paymentProcessYear) {
	super(paymentProcessYear);
    }

    @Override
    public void calculateData() {
        super.calculateData();
	final SortedMap<AcquisitionProcessStateType,Integer> counts = processCounter.getCounts();
	for (final Entry<AcquisitionProcessStateType,Integer> entry : counts.entrySet()) {
	    final String key = entry.getKey().getLocalizedName();
	    final Number value = entry.getValue();
	    if (value.doubleValue() > 0) {
		registerData(key, value);
	    }
	}
    }

    @Override
    protected void count(final PaymentProcess paymentProcess) {
	if (paymentProcess.isSimplifiedProcedureProcess()) {
	    final SimplifiedProcedureProcess simplifiedProcedureProcess = (SimplifiedProcedureProcess) paymentProcess;
	    final AcquisitionProcessStateType type = simplifiedProcedureProcess.getAcquisitionProcessStateType();
	    processCounter.count(type);
	}
    }

}
