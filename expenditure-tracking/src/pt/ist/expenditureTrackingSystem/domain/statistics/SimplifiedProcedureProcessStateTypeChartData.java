package pt.ist.expenditureTrackingSystem.domain.statistics;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.util.Calculation.Operation;

public abstract class SimplifiedProcedureProcessStateTypeChartData extends PaymentProcessChartData<AcquisitionProcessStateType> {

    public SimplifiedProcedureProcessStateTypeChartData(final PaymentProcessYear paymentProcessYear, final Operation operation) {
	super(paymentProcessYear, operation);
    }

    @Override
    protected AcquisitionProcessStateType[] getCategories() {
	return AcquisitionProcessStateType.values();
    };

    @Override
    protected String getLabel(final AcquisitionProcessStateType acquisitionProcessStateType) {
	return acquisitionProcessStateType.getLocalizedName();
    }

}
