package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.OperationLog;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;

public abstract class GenericAcquisitionProcessActivity extends AbstractActivity<AcquisitionProcess> {

    @Override
    protected void logExecution(AcquisitionProcess process, String operationName, User user) {
	new OperationLog(process, user.getPerson(), operationName, process.getAcquisitionProcessStateType(), new DateTime());
    }

}
