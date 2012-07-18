/*
 * @(#)Util.java
 *
 * Copyright 2009 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified;

import java.util.TreeSet;

import module.workflow.domain.ActivityLog;
import module.workflow.domain.WorkflowLog;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class Util {

    public static boolean isAppiableForYear(final int year, final SimplifiedProcedureProcess simplifiedProcedureProcess) {
	final TreeSet<ActivityLog> logs = new TreeSet<ActivityLog>(WorkflowLog.COMPARATOR_BY_WHEN_REVERSED);
	for (WorkflowLog log : simplifiedProcedureProcess.getExecutionLogs(ActivityLog.class)) {
	    logs.add((ActivityLog) log);
	}
	for (final ActivityLog genericLog : logs) {
	    if (genericLog.getOperation().equals("RevertSkipPurchaseOrderDocument")) {
		return false;
	    }
	    if (genericLog.getWhenOperationWasRan().getYear() == year && matchesAppiableForYearActivity(year, genericLog)) {
		return true;
	    }
	}
	return false;
    }

    public static boolean isAppiableForYear(final int year, final RefundProcess refundProcess) {
	// TODO : implement this properly... until then always count
	// everything... which will work because there is still only one year...
	// :)
	// Currently I'm not sure whether this should be based on the invoice
	// date, or some authorization date.
	return year == 2009;
    }

    private static boolean matchesAppiableForYearActivity(final int year, final ActivityLog genericLog) {
	return genericLog.getOperation().equals("SendAcquisitionRequestToSupplier")
		|| genericLog.getOperation().equals("SendPurchaseOrderToSupplier")
		|| genericLog.getOperation().equals("SkipPurchaseOrderDocument");
    }

}
