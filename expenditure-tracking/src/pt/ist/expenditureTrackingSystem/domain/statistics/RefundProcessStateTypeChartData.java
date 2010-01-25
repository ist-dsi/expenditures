package pt.ist.expenditureTrackingSystem.domain.statistics;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.util.Calculation.Operation;

public abstract class RefundProcessStateTypeChartData extends PaymentProcessChartData<RefundProcessStateType> {

    public RefundProcessStateTypeChartData(final PaymentProcessYear paymentProcessYear, final Operation operation) {
	super(paymentProcessYear, operation);
    }

    @Override
    protected RefundProcessStateType[] getCategories() {
	return RefundProcessStateType.values();
    };

    @Override
    protected String getLabel(final RefundProcessStateType refundProcessStateType) {
	return refundProcessStateType.getLocalizedName();
    }

}
