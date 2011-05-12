package module.workingCapital.domain.util;

import module.workflow.domain.ProcessCounter;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalSystem;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;

public class WorkingCapitalPendingProcessCounter extends ProcessCounter {

    public WorkingCapitalPendingProcessCounter() {
	super(WorkingCapitalProcess.class);
    }

    @Override
    public int getCount() {
	int result = 0;
	final User user = UserView.getCurrentUser();
	for (final WorkingCapital workingCapital : WorkingCapitalSystem.getInstanceForCurrentHost().getWorkingCapitalsSet()) {
	    final WorkingCapitalProcess workingCapitalProcess = workingCapital.getWorkingCapitalProcess();
	    if (shouldCountProcess(workingCapitalProcess, user)) {
		result++;
	    }
	}
	return result;
    }

}
