package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class SimplifiedProcessTotalValueStatistics implements Serializable {

    final Map<AcquisitionProcessStateType, Money> result = new TreeMap<AcquisitionProcessStateType, Money>();

    private int numberOfProcesses = 0;

    public SimplifiedProcessTotalValueStatistics(final PaymentProcessYear acquisitionProcessYear) {
	for (AcquisitionProcessStateType acquisitionProcessStateType : AcquisitionProcessStateType.values()) {
	    result.put(acquisitionProcessStateType, Money.ZERO);
	}

	for (final PaymentProcess paymentProcess : acquisitionProcessYear.getPaymentProcessSet()) {
	    if (paymentProcess.isSimplifiedProcedureProcess()) {
		numberOfProcesses++;
		final AcquisitionProcessStateType acquisitionProcessStateType = ((SimplifiedProcedureProcess) paymentProcess)
			.getAcquisitionProcessStateType();
		result.put(acquisitionProcessStateType, result.get(acquisitionProcessStateType).add(
			paymentProcess.getRequest().getTotalValue()));
	    }
	}
    }

    public static SimplifiedProcessTotalValueStatistics create(final Integer year) {
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

    private static SimplifiedProcessTotalValueStatistics create(final PaymentProcessYear paymentProcessYear) {
	return new SimplifiedProcessTotalValueStatistics(paymentProcessYear);
    }

    public int getNumberOfProcesses() {
	return numberOfProcesses;
    }

    public Map<AcquisitionProcessStateType, Money> getTotalValuesOfProcessesByAcquisitionProcessStateType() {
	return result;
    }

}
