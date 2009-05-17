package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified;

import java.util.TreeSet;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericLog;

public class Util {

    public static boolean isAppiableForYear(final int year, final SimplifiedProcedureProcess simplifiedProcedureProcess) {
	final TreeSet<GenericLog> logs = new TreeSet<GenericLog>(GenericLog.COMPARATOR_BY_WHEN_REVERSED);
	logs.addAll(simplifiedProcedureProcess.getExecutionLogsSet());
	for (final GenericLog genericLog : logs) {
	    if (genericLog.getOperation().equals("RevertSkipPurchaseOrderDocument")) {
		return false;
	    }
	    if (genericLog.getWhenOperationWasRan().getYear() == year
		    && matchesAppiableForYearActivity(year, genericLog)) {
		return true;
	    }
	}
	return false;
    }

    public static boolean isAppiableForYear(final int year, final RefundProcess refundProcess) {
	// TODO : implement this properly... until then always count everything... which will work because there is still only one year... :)
	// Currently I'm not sure whether this should be based on the invoice date, or some authorization date.
	return true;
    }

    private static boolean matchesAppiableForYearActivity(final int year, final GenericLog genericLog) {
	return genericLog.getOperation().equals("SendAcquisitionRequestToSupplier")
	    || genericLog.getOperation().equals("SendPurchaseOrderToSupplier")
	    || genericLog.getOperation().equals("SkipPurchaseOrderDocument");
    }

}
