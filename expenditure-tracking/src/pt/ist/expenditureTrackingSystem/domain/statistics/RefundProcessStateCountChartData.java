package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.util.Calculation.Operation;

public class RefundProcessStateCountChartData extends RefundProcessStateTypeChartData {

    public RefundProcessStateCountChartData(final PaymentProcessYear paymentProcessYear) {
	super(paymentProcessYear, Operation.SUM);
    }

    @Override
    protected String getTitleKey() {
	return "label.number.processes";
    }

    @Override
    protected void count(final PaymentProcess paymentProcess) {
	if (paymentProcess.isRefundProcess()) {
	    final RefundProcess refundProcess = (RefundProcess) paymentProcess;
	    final RefundProcessStateType refundProcessStateType = refundProcess.getProcessState().getRefundProcessStateType();
	    calculation.registerValue(refundProcessStateType, new BigDecimal(1));
	}
    }

}
