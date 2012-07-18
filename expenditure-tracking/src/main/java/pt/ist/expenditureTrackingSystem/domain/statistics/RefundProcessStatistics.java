package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class RefundProcessStatistics implements Serializable {

    private int numberOfProcesses = 0;
    private int[] numberOfProcessesByRefundProcessStateType = new int[RefundProcessStateType.values().length];

    public RefundProcessStatistics(final PaymentProcessYear acquisitionProcessYear) {
	for (int i = 0; i < RefundProcessStateType.values().length; i++) {
	    numberOfProcessesByRefundProcessStateType[i] = 0;
	}
	for (final PaymentProcess paymentProcess : acquisitionProcessYear.getPaymentProcessSet()) {
	    if (paymentProcess.isRefundProcess()) {
		final RefundProcess refundProcess = (RefundProcess) paymentProcess;
		accountFor(refundProcess);
	    }
	}
    }

    private void accountFor(final RefundProcess refundProcess) {
	numberOfProcesses++;
	final RefundProcessStateType refundProcessStateType = refundProcess.getProcessState().getRefundProcessStateType();
	numberOfProcessesByRefundProcessStateType[refundProcessStateType.ordinal()]++;
    }

    public static RefundProcessStatistics create(final Integer year) {
	if (year != null) {
	    final int y = year.intValue();
	    for (final PaymentProcessYear paymentProcessYear : ExpenditureTrackingSystem.getInstance().getPaymentProcessYearsSet()) {
		if (paymentProcessYear.getYear().intValue() == y) {
		    return create(paymentProcessYear);
		}
	    }
	}
	return null;
    }

    private static RefundProcessStatistics create(final PaymentProcessYear paymentProcessYear) {
	return new RefundProcessStatistics(paymentProcessYear);
    }

    public int getNumberOfProcesses() {
        return numberOfProcesses;
    }

    public Map<RefundProcessStateType, Integer> getNumberOfProcessesByRefundProcessStateType() {
	final Map<RefundProcessStateType, Integer> map = new TreeMap<RefundProcessStateType, Integer>();
	for (final RefundProcessStateType refundProcessStateType : RefundProcessStateType.values()) {
	    map.put(refundProcessStateType, Integer.valueOf(numberOfProcessesByRefundProcessStateType[refundProcessStateType.ordinal()]));
	}
        return map;
    }

}
