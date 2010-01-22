package pt.ist.expenditureTrackingSystem.domain.statistics;

import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;

public abstract class PaymentProcessChartData extends ChartData {

    protected final PaymentProcessYear paymentProcessYear;

    public PaymentProcessChartData(final PaymentProcessYear paymentProcessYear) {
	this.paymentProcessYear = paymentProcessYear;
	setTitle(BundleUtil.getStringFromResourceBundle("resources.AcquisitionResources", "label.number.processes"));
    }

    public void calculateData() {
	for (final PaymentProcess paymentProcess : paymentProcessYear.getPaymentProcessSet()) {
	    count(paymentProcess);
	}
    }

    protected abstract void count(final PaymentProcess paymentProcess);

}
