package pt.ist.expenditureTrackingSystem.util;

import module.workflow.domain.ProcessCounter;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Acquisition;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.standard.StandardProcedureProcess;

public class AquisitionsPendingProcessCounter extends ProcessCounter {

    public AquisitionsPendingProcessCounter() {
	super(AcquisitionProcess.class);
    }

    @Override
    public Class getProcessClassForForwarding() {
        return StandardProcedureProcess.class;
    }

    @Override
    public int getCount() {
	int result = 0;
	final User user = UserView.getCurrentUser();
	
	for (final Acquisition acquisition : ExpenditureTrackingSystem.getInstance().getAcquisitionsSet()) {
	    if (acquisition instanceof AcquisitionRequest) {
		final AcquisitionRequest acquisitionRequest = (AcquisitionRequest) acquisition;
		final AcquisitionProcess acquisitionProcess = acquisitionRequest.getAcquisitionProcess();
		if (shouldCountProcess(acquisitionProcess, user)) {
		    result++;
		}
	    }
	}
	return result;
    }

}
