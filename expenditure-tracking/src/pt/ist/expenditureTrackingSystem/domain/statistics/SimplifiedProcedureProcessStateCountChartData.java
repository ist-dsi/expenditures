package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.util.Calculation.Operation;

public class SimplifiedProcedureProcessStateCountChartData extends SimplifiedProcedureProcessStateTypeChartData {

    public SimplifiedProcedureProcessStateCountChartData(final PaymentProcessYear paymentProcessYear) {
	super(paymentProcessYear, Operation.SUM);
    }

    @Override
    protected String getTitleKey() {
	return "label.number.processes";
    }

    @Override
    protected void count(final PaymentProcess paymentProcess) {
	if (paymentProcess.isSimplifiedProcedureProcess()) {
	    final SimplifiedProcedureProcess simplifiedProcedureProcess = (SimplifiedProcedureProcess) paymentProcess;
	    final AcquisitionProcessStateType acquisitionProcessStateType = simplifiedProcedureProcess.getAcquisitionProcessStateType();
	    calculation.registerValue(acquisitionProcessStateType, new BigDecimal(1));
	}
    }

}
