package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;

public class RevertToInvoiceConfirmation extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(final RegularAcquisitionProcess process) {
	return process.isProjectAccountingEmployee();
    }

    @Override
    protected boolean isAvailable(final RegularAcquisitionProcess process) {
	return isCurrentUserProcessOwner(process) && process.isInvoiceConfirmed() && allItemsAreFilledWithRealValues(process)
		&& process.getRequest().isEveryItemFullyAttributeInRealValues()
		&& !process.hasAllocatedFundsPermanentlyForAllProjectFinancers();
    }

    private boolean allItemsAreFilledWithRealValues(final RegularAcquisitionProcess process) {
	for (final RequestItem requestItem : process.getRequest().getRequestItemsSet()) {
	    if (!requestItem.isFilledWithRealValues()) {
		return false;
	    }
	}
	return true;
    }

    @Override
    protected void process(final RegularAcquisitionProcess process, final Object... objects) {
	process.unconfirmInvoiceForAll();
    }

}
