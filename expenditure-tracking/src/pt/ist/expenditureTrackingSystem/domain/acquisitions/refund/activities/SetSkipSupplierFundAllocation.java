package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class SetSkipSupplierFundAllocation extends GenericRefundProcessActivity {

    @Override
    protected boolean isAccessible(RefundProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null
		&& (loggedPerson == process.getRequestor() || userHasRole(RoleType.SUPPLIER_FUND_ALLOCATION_MANAGER));
    }

    @Override
    protected boolean isAvailable(RefundProcess process) {
	return super.isAvailable(process)
		&& !process.getSkipSupplierFundAllocation()
		&& (((process.isInGenesis() ||process.getProcessState().getRefundProcessStateType() == RefundProcessStateType.AUTHORIZED) && getLoggedPerson() == process.getRequestor()) 
			|| userHasRole(RoleType.SUPPLIER_FUND_ALLOCATION_MANAGER));

    }

    @Override
    protected void process(RefundProcess process, Object... objects) {
	process.setSkipSupplierFundAllocation(Boolean.TRUE);
    }
}
