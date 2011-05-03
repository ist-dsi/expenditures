package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.AbstractDistributeRealValuesForPayingUnits;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class DistributeRealValuesForPayingUnits extends AbstractDistributeRealValuesForPayingUnits<RefundProcess> {

    @Override
    public boolean isActive(RefundProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user)
		&& process.isAnyRefundInvoiceAvailable()
		&& ((process.getRequestor() == person && process.isInAuthorizedState())
			|| (process.isPendingInvoicesConfirmation()
				&& ((ExpenditureTrackingSystem.isAccountingManagerGroupMember(user)
					&& !process.hasProjectsAsPayingUnits())
				|| (ExpenditureTrackingSystem.isProjectAccountingManagerGroupMember(user)
					&& process.hasProjectsAsPayingUnits()))));
    }

}
