/*
 * @(#)SimplifiedProcedureProcessStateTimeChartData.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.util.Calculation.Operation;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class SimplifiedProcedureProcessStateTimeChartData extends SimplifiedProcedureProcessStateTypeChartData {

    public SimplifiedProcedureProcessStateTimeChartData(final PaymentProcessYear paymentProcessYear) {
	super(paymentProcessYear, Operation.MEDIAN);
    }

    @Override
    protected String getTitleKey() {
	return "label.process.state.times.median";
    }

    @Override
    protected void registerData(final String key, final Number number) {
	final BigDecimal value = ((BigDecimal) number).divide(DAYS_CONST, 100, BigDecimal.ROUND_HALF_UP);
        super.registerData(key, value);
    }

    @Override
    protected void count(final PaymentProcess paymentProcess) {
	if (paymentProcess.isSimplifiedProcedureProcess()) {
	    final SimplifiedProcedureProcess simplifiedProcedureProcess = (SimplifiedProcedureProcess) paymentProcess;

	    final List<ProcessState> processStates = new ArrayList<ProcessState>(simplifiedProcedureProcess.getProcessStatesSet());
	    Collections.sort(processStates, ProcessState.COMPARATOR_BY_WHEN);
	    final long[] durations = new long[AcquisitionProcessStateType.values().length];
	    for (int i = 1; i < processStates.size(); i++) {
		final AcquisitionProcessState processState = (AcquisitionProcessState) processStates.get(i);
		final AcquisitionProcessState previousState = (AcquisitionProcessState) processStates.get(i - 1);
		final DateTime stateChangeDateTime = processState.getWhenDateTime();
		final DateTime previousStateChangeDateTime = previousState.getWhenDateTime();
		final long startMillis = stateChangeDateTime.getMillis();
		final long duration = startMillis - previousStateChangeDateTime.getMillis();

		final AcquisitionProcessStateType acquisitionProcessStateType = previousState.getAcquisitionProcessStateType();
		durations[acquisitionProcessStateType.ordinal()] += duration;
		//calculation.registerValue(acquisitionProcessStateType, new BigDecimal(duration));
	    }
	    for (final AcquisitionProcessStateType acquisitionProcessStateType : AcquisitionProcessStateType.values()) {
		final long duration = durations[acquisitionProcessStateType.ordinal()];
		if (duration > 0) {
		    calculation.registerValue(acquisitionProcessStateType, new BigDecimal(duration));
		}
	    }
	}
    }

}
