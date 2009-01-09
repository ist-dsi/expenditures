package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class SimplifiedProcessStatistics implements Serializable {

    private int numberOfProcesses = 0;
    private int[] numberOfProcessesByAcquisitionProcessStateType = new int[AcquisitionProcessStateType.values().length];

    public SimplifiedProcessStatistics(final AcquisitionProcessYear acquisitionProcessYear) {
	for (int i = 0; i < AcquisitionProcessStateType.values().length; i++) {
	    numberOfProcessesByAcquisitionProcessStateType[i] = 0;
	}
	for (final AcquisitionProcess acquisitionProcess : acquisitionProcessYear.getAcquisitionProcessesSet()) {
	    if (acquisitionProcess.isSimplifiedProcedureProcess()) {
		final SimplifiedProcedureProcess simplifiedProcedureProcess = (SimplifiedProcedureProcess) acquisitionProcess;
		accountFor(simplifiedProcedureProcess);
	    }
	}
    }

    private void accountFor(final SimplifiedProcedureProcess simplifiedProcedureProcess) {
	numberOfProcesses++;
	final AcquisitionProcessStateType acquisitionProcessStateType = simplifiedProcedureProcess.getAcquisitionProcessStateType();
	numberOfProcessesByAcquisitionProcessStateType[acquisitionProcessStateType.ordinal()]++;
    }

    public static SimplifiedProcessStatistics create(final Integer year) {
	if (year != null) {
	    final int y = year.intValue();
	    for (final AcquisitionProcessYear acquisitionProcessYear : ExpenditureTrackingSystem.getInstance().getAcquisitionProcessYearsSet()) {
		if (acquisitionProcessYear.getYear().intValue() == y) {
		    return create(acquisitionProcessYear);
		}
	    }
	}
	return null;
    }

    private static SimplifiedProcessStatistics create(final AcquisitionProcessYear acquisitionProcessYear) {
	return new SimplifiedProcessStatistics(acquisitionProcessYear);
    }

    public int getNumberOfProcesses() {
        return numberOfProcesses;
    }

    public Map<AcquisitionProcessStateType, Integer> getNumberOfProcessesByAcquisitionProcessStateType() {
	final Map<AcquisitionProcessStateType, Integer> map = new TreeMap<AcquisitionProcessStateType, Integer>();
	for (final AcquisitionProcessStateType acquisitionProcessStateType : SimplifiedProcedureProcess.getAvailableStatesForSimplifiedProcedureProcess()) {
	    map.put(acquisitionProcessStateType, Integer.valueOf(numberOfProcessesByAcquisitionProcessStateType[acquisitionProcessStateType.ordinal()]));
	}
        return map;
    }

}
