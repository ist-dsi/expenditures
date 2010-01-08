package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.util.Collections;
import java.util.List;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowLog;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.OperationLog;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

public class RemoveCancelProcess extends
	WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	return user.getExpenditurePerson().hasRoleType(RoleType.MANAGER) && isUserProcessOwner(process, user)
		&& process.getAcquisitionProcessState().isCanceled();
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
	RegularAcquisitionProcess process = activityInformation.getProcess();
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

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
	return "resources/AcquisitionResources";
    }

    @Override
    public boolean isUserAwarenessNeeded(RegularAcquisitionProcess process, User user) {
	return false;
    }
}
