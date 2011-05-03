package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;

public class RemoveCancelProcess<P extends PaymentProcess> extends WorkflowActivity<P, ActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
	return user != null
		&& ExpenditureTrackingSystem.isManager()
		&& process.isCanceled();
    }

    @Override
    protected void process(ActivityInformation<P> activityInformation) {
	final PaymentProcess process = activityInformation.getProcess();
	final List<ProcessState> states = new ArrayList<ProcessState>(process.getProcessStates());
	if (states.size() > 1) {
	    Collections.sort(states, ProcessState.COMPARATOR_BY_WHEN);
	    final int previous = states.size() - 2;
	    final ProcessState processState = states.get(previous);
	    process.revertToState(processState);
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

    public boolean isUserAwarenessNeeded(P process, User user) {
	return false;
    }

}
