package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class UnconfirmInvoices extends GenericRefundProcessActivity {

    @Override
    protected boolean isAccessible(RefundProcess process) {
	return process.isRealValueFullyAttributedToUnits()
		&& ((process.isAccountingEmployee() && !process.hasProjectsAsPayingUnits()) || (process
			.isProjectAccountingEmployee() && process.hasProjectsAsPayingUnits()))
		&& !process.getRequest().getConfirmedInvoices().isEmpty();
    }

    @Override
    protected void process(RefundProcess process, Object... objects) {
	Person person = getLoggedPerson();
	process.unconfirmInvoicesByPerson(person);

    }

}