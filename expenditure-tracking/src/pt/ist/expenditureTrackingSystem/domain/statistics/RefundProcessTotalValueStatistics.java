package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class RefundProcessTotalValueStatistics implements Serializable {

    final Map<RefundProcessStateType, Money> result = new TreeMap<RefundProcessStateType, Money>();

    private int numberOfProcesses = 0;

    public RefundProcessTotalValueStatistics(final PaymentProcessYear acquisitionProcessYear) {
	for (RefundProcessStateType stateType : RefundProcessStateType.values()) {
	    result.put(stateType, Money.ZERO);
	}

	for (final PaymentProcess paymentProcess : acquisitionProcessYear.getPaymentProcessSet()) {
	    if (paymentProcess.isRefundProcess()) {
		numberOfProcesses++;
		final RefundProcessStateType refundProcessStateType = ((RefundProcess) paymentProcess).getProcessState()
			.getRefundProcessStateType();
		result.put(refundProcessStateType, result.get(refundProcessStateType).add(
			paymentProcess.getRequest().getTotalValue()));
	    }
	}
    }

    public static RefundProcessTotalValueStatistics create(final Integer year) {
	if (year != null) {
	    final int y = year.intValue();
	    for (final PaymentProcessYear paymentProcessYear : ExpenditureTrackingSystem.getInstance()
		    .getPaymentProcessYearsSet()) {
		if (paymentProcessYear.getYear().intValue() == y) {
		    return create(paymentProcessYear);
		}
	    }
	}
	return null;
    }

    private static RefundProcessTotalValueStatistics create(final PaymentProcessYear paymentProcessYear) {
	return new RefundProcessTotalValueStatistics(paymentProcessYear);
    }

    public int getNumberOfProcesses() {
	return numberOfProcesses;
    }

    public Map<RefundProcessStateType, Money> getTotalValuesOfProcessesByRefundProcessStateType() {
	return result;
    }

}
