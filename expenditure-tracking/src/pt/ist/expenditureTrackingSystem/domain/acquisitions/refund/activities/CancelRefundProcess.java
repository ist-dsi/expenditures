package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class CancelRefundProcess extends GenericRefundProcessActivity {

    @Override
    protected boolean isAvailable(RefundProcess process) {
	return super.isAvailable(process) && (isCurrentUserRequestor(process) || isUserResponsibleForUnit(process));
    }

    @Override
    protected boolean isAccessible(RefundProcess process) {
	return (process.isInGenesis() && isCurrentUserRequestor(process))
		|| (process.isPendingApproval() && isUserResponsibleForUnit(process));
    }

    private boolean isUserResponsibleForUnit(RefundProcess process) {
	User user = getUser();
	return user != null && process.isResponsibleForAtLeastOnePayingUnit(user.getPerson());
    }

    @Override
    protected void process(RefundProcess process, Object... objects) {
	process.cancel();
    }

}
