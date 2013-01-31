package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class SimplifiedProcessStatistics implements Serializable {

	private int numberOfProcesses = 0;
	private int[] numberOfProcessesByAcquisitionProcessStateType = new int[AcquisitionProcessStateType.values().length];

	public SimplifiedProcessStatistics(final PaymentProcessYear acquisitionProcessYear) {
		for (int i = 0; i < AcquisitionProcessStateType.values().length; i++) {
			numberOfProcessesByAcquisitionProcessStateType[i] = 0;
		}
		for (final PaymentProcess paymentProcess : acquisitionProcessYear.getPaymentProcessSet()) {
			if (paymentProcess.isSimplifiedProcedureProcess()) {
				final SimplifiedProcedureProcess simplifiedProcedureProcess = (SimplifiedProcedureProcess) paymentProcess;
				accountFor(simplifiedProcedureProcess);
			}
		}
	}

	private void accountFor(final SimplifiedProcedureProcess simplifiedProcedureProcess) {
		numberOfProcesses++;
		final AcquisitionProcessStateType acquisitionProcessStateType =
				simplifiedProcedureProcess.getAcquisitionProcessStateType();
		numberOfProcessesByAcquisitionProcessStateType[acquisitionProcessStateType.ordinal()]++;
	}

	public static SimplifiedProcessStatistics create(final Integer year) {
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

	private static SimplifiedProcessStatistics create(final PaymentProcessYear paymentProcessYear) {
		return new SimplifiedProcessStatistics(paymentProcessYear);
	}

	public int getNumberOfProcesses() {
		return numberOfProcesses;
	}

	public Map<AcquisitionProcessStateType, Integer> getNumberOfProcessesByAcquisitionProcessStateType() {
		final Map<AcquisitionProcessStateType, Integer> map = new TreeMap<AcquisitionProcessStateType, Integer>();
		for (final AcquisitionProcessStateType acquisitionProcessStateType : SimplifiedProcedureProcess
				.getAvailableStatesForSimplifiedProcedureProcess()) {
			map.put(acquisitionProcessStateType,
					Integer.valueOf(numberOfProcessesByAcquisitionProcessStateType[acquisitionProcessStateType.ordinal()]));
		}
		return map;
	}

}
