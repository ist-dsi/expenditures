package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;

public class RemoveFundsPermanentlyAllocated<T extends PaymentProcess> extends AbstractActivity<T> {

    @Override
    protected boolean isAccessible(final T process) {
	return process.isAccountingEmployee();
    }

    @Override
    protected boolean isAvailable(final T process) {
	return isCurrentUserProcessOwner(process) && process.isAllocatedPermanently();
    }

    @Override
    protected void process(final T process, final Object... objects) {
	process.resetEffectiveFundAllocationId();
    }
}
