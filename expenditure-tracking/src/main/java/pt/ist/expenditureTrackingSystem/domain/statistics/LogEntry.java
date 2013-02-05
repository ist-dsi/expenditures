/*
 * @(#)LogEntry.java
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

import module.workflow.activities.WorkflowActivity;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class LogEntry {

    private final WorkflowActivity abstractActivity;

    private long duration = 0;

    private int numberOfProcesses = 0;

    public LogEntry(final WorkflowActivity abstractActivity) {
        this.abstractActivity = abstractActivity;
    }

    public void countProcess() {
        numberOfProcesses++;
    }

    public void register(final long duration) {
        this.duration += duration;
    }

    public WorkflowActivity getAbstractActivity() {
        return abstractActivity;
    }

    private static final BigDecimal DAYS_CONST = new BigDecimal(1000 * 3600 * 24);

    public BigDecimal getDays() {
        final BigDecimal bigDecimal = new BigDecimal(duration / numberOfProcesses);
        return bigDecimal.divide(DAYS_CONST, 100, BigDecimal.ROUND_HALF_UP);
    }

}
