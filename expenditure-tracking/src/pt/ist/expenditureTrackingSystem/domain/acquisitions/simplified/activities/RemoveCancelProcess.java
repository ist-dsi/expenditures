package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.util.Collections;
import java.util.List;

import module.workflow.domain.WorkflowLog;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.OperationLog;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class RemoveCancelProcess extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && loggedPerson.hasRoleType(RoleType.MANAGER);
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	final AcquisitionRequest acquisitionRequest = process.getAcquisitionRequest();
	return super.isAvailable(process) && process.getAcquisitionProcessState().isCanceled();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	List<OperationLog> logs = process.getOperationLogs();
	Collections.sort(logs, WorkflowLog.COMPARATOR_BY_WHEN);
	for (int i = logs.size(); i > 0; i--) {
	    final OperationLog operationLog = logs.get(i - 1);
	    final AcquisitionProcessStateType acquisitionProcessStateType = operationLog.getState();
	    if (acquisitionProcessStateType != null && acquisitionProcessStateType != AcquisitionProcessStateType.CANCELED) {
		new AcquisitionProcessState(process, acquisitionProcessStateType);
		break;
	    }
	}
    }

}
