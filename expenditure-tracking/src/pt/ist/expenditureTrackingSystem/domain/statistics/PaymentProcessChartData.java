package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.math.BigDecimal;
import java.util.SortedMap;
import java.util.Map.Entry;

import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.util.Calculation;
import pt.ist.expenditureTrackingSystem.util.Calculation.Operation;

public abstract class PaymentProcessChartData<C extends Comparable<C>> extends ChartData {

    protected final BigDecimal DAYS_CONST = new BigDecimal(1000 * 3600 * 24);

    protected final PaymentProcessYear paymentProcessYear;

    protected Calculation<C> calculation;

    private final Operation operation;

    public PaymentProcessChartData(final PaymentProcessYear paymentProcessYear, final Operation operation) {
	this.paymentProcessYear = paymentProcessYear;
	this.operation = operation;
	setTitle(BundleUtil.getStringFromResourceBundle("resources.AcquisitionResources", getTitleKey()));
	calculation = new Calculation<C>(getCategories(), operation);
    }

    public void calculateData() {
	for (final PaymentProcess paymentProcess : paymentProcessYear.getPaymentProcessSet()) {
	    count(paymentProcess);
	}

	final SortedMap<C, BigDecimal> result = calculation.getResult(operation);
	for (Entry<C, BigDecimal> entry : result.entrySet()) {
	    final String key = getLabel(entry.getKey());
	    final BigDecimal value = entry.getValue();
	    registerData(key, value);
	}
    }

    protected abstract String getTitleKey();

    protected abstract C[] getCategories();

    protected abstract String getLabel(final C c);

    protected abstract void count(final PaymentProcess paymentProcess);

    public SortedMap<C, BigDecimal> getResults(final Operation operation) {
	return calculation.getResult(operation);
    }

}
