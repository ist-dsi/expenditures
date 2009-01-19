package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.AbstractDistributeRealValuesForPayingUnits;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class DistributeRealValuesForPayingUnits extends AbstractDistributeRealValuesForPayingUnits<RefundProcess> {

    @Override
    protected boolean isAccessible(RefundProcess process) {
	return isCurrentUserProcessOwner(process) || userHasRole(RoleType.ACCOUNTING_MANAGER)
		|| userHasRole(RoleType.PROJECT_ACCOUNTING_MANAGER);
    }

    @Override
    protected boolean isAvailable(RefundProcess process) {
	return (isCurrentUserProcessOwner(process) && process.isInAuthorizedState())
		|| process.isPendingInvoicesConfirmation()
		&& ((userHasRole(RoleType.ACCOUNTING_MANAGER) && !process.hasProjectsAsPayingUnits()) || (userHasRole(RoleType.PROJECT_ACCOUNTING_MANAGER) && process
			.hasProjectsAsPayingUnits()));
    }

}
