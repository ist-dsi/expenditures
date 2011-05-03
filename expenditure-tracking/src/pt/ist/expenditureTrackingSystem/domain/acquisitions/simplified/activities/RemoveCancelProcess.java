package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

public class RemoveCancelProcess extends
	WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	return ExpenditureTrackingSystem.isManager()
		&& isUserProcessOwner(process, user)
		&& process.getAcquisitionProcessState().isCanceled();
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
	RegularAcquisitionProcess process = activityInformation.getProcess();
	List<ProcessState> states = new ArrayList<ProcessState>(process.getProcessStates());
	Collections.sort(states, ProcessState.COMPARATOR_BY_WHEN);

	for (int i = states.size(); i > 0; i--) {

	    final AcquisitionProcessState state = (AcquisitionProcessState) states.get(i - 1);

	    if (!state.isCanceled()) {
		new AcquisitionProcessState(process, state.getAcquisitionProcessStateType());
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
