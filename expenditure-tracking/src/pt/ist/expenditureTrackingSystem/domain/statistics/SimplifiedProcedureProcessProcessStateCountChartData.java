package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.util.SortedMap;
import java.util.Map.Entry;

import myorg.util.BundleUtil;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.util.AcquisitionProcessStateTypeCounter;

public class SimplifiedProcedureProcessProcessStateCountChartData extends PaymentProcessChartData {

    protected final AcquisitionProcessStateTypeCounter processCounter = new AcquisitionProcessStateTypeCounter();

    public SimplifiedProcedureProcessProcessStateCountChartData(PaymentProcessYear paymentProcessYear) {
	super(paymentProcessYear);
	setTitle(BundleUtil.getStringFromResourceBundle("resources.AcquisitionResources", "label.number.processes"));
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
