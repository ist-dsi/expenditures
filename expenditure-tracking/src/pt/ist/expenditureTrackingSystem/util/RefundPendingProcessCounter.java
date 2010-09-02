package pt.ist.expenditureTrackingSystem.util;

import module.workflow.domain.ProcessCounter;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Acquisition;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundRequest;

public class RefundPendingProcessCounter extends ProcessCounter {

    public RefundPendingProcessCounter() {
	super(RefundProcess.class);
    }

    @Override
    public int getCount() {
	int result = 0;
	final User user = UserView.getCurrentUser();
	
	for (final Acquisition acquisition : ExpenditureTrackingSystem.getInstance().getAcquisitionsSet()) {
	    if (acquisition instanceof RefundRequest) {
		final RefundRequest refundRequest = (RefundRequest) acquisition;
		final RefundProcess process = refundRequest.getProcess();
		if (shouldCountProcess(process, user)) {
		    result++;
		}
	    }
	}
	return result;
    }

}
