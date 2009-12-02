package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import myorg.domain.exceptions.DomainException;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

public class UnsetSkipSupplierFundAllocation extends GenericRefundProcessActivity {

    @Override
    protected boolean isAccessible(RefundProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null
		&& (loggedPerson == process.getRequestor() || userHasRole(RoleType.ACQUISITION_CENTRAL) || userHasRole(RoleType.SUPPLIER_FUND_ALLOCATION_MANAGER));
    }

    @Override
    protected boolean isAvailable(RefundProcess process) {
	return super.isAvailable(process)
		&& process.getSkipSupplierFundAllocation()
		&& (((process.isInGenesis() || process.getProcessState().getRefundProcessStateType() == RefundProcessStateType.AUTHORIZED) && getLoggedPerson() == process
			.getRequestor()) || userHasRole(RoleType.SUPPLIER_FUND_ALLOCATION_MANAGER));

    }

    @Override
    protected void process(RefundProcess process, Object... objects) {
	for (Supplier supplier : process.getRequest().getSuppliers()) {
	    if (!supplier.isFundAllocationAllowed(process.getRequest().getTotalValue())) {
		throw new DomainException("acquisitionProcess.message.exception.SupplierDoesNotAlloweAmount");
	    }
	}
	process.setSkipSupplierFundAllocation(Boolean.FALSE);
    }
}
