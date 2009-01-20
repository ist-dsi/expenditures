package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class ConfirmInvoices extends GenericRefundProcessActivity {

    @Override
    protected boolean isAccessible(RefundProcess process) {
	return process.isRealValueFullyAttributedToUnits()
		&& ((process.isAccountingEmployee() && !process.hasProjectsAsPayingUnits()) || (process
			.isProjectAccountingEmployee() && process.hasProjectsAsPayingUnits()))
		&& process.isPendingInvoicesConfirmation();
    }

    @Override
    protected void process(RefundProcess process, Object... objects) {
	process.confirmInvoices();
    }

}
