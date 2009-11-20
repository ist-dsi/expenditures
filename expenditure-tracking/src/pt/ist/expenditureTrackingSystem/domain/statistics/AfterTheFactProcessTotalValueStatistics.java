package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionType;

public class AfterTheFactProcessTotalValueStatistics implements Serializable {

    final Map<AfterTheFactAcquisitionType, Money> result = new TreeMap<AfterTheFactAcquisitionType, Money>();

    private int numberOfProcesses = 0;

    public AfterTheFactProcessTotalValueStatistics(final PaymentProcessYear acquisitionProcessYear) {
	for (AfterTheFactAcquisitionType stateType : AfterTheFactAcquisitionType.values()) {
	    result.put(stateType, Money.ZERO);
	}

	for (final PaymentProcess paymentProcess : acquisitionProcessYear.getPaymentProcessSet()) {

	    if (paymentProcess instanceof AfterTheFactAcquisitionProcess) {
		final AfterTheFactAcquisitionProcess process = (AfterTheFactAcquisitionProcess) paymentProcess;

		if (process.hasAcquisitionAfterTheFact() && !process.getAcquisitionAfterTheFact().getDeletedState()) {
		    numberOfProcesses++;

		    final AfterTheFactAcquisitionType afterTheFactAcquisitionType = process.getAcquisitionAfterTheFact()
			    .getAfterTheFactAcquisitionType();
		    result.put(afterTheFactAcquisitionType, result.get(afterTheFactAcquisitionType).add(
			    process.getAcquisitionAfterTheFact().getValue()));
		}
	    }
	}
    }

    public static AfterTheFactProcessTotalValueStatistics create(final Integer year) {
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

    private static AfterTheFactProcessTotalValueStatistics create(final PaymentProcessYear paymentProcessYear) {
	return new AfterTheFactProcessTotalValueStatistics(paymentProcessYear);
    }

    public int getNumberOfProcesses() {
	return numberOfProcesses;
    }

    public Map<AfterTheFactAcquisitionType, Money> getTotalValuesOfProcessesByAquisitionProcessStateType() {
	return result;
    }

}
