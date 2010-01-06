package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;

public class WorkingCapitalInitializationInformation extends ActivityInformation<WorkingCapitalProcess> {

    private WorkingCapitalInitialization workingCapitalInitialization;

    public WorkingCapitalInitializationInformation(final WorkingCapitalProcess workingCapitalProcess,
	    final WorkflowActivity<WorkingCapitalProcess, ? extends ActivityInformation<WorkingCapitalProcess>> activity) {
	super(workingCapitalProcess, activity);
    }

    public WorkingCapitalInitialization getWorkingCapitalInitialization() {
        return workingCapitalInitialization;
    }

    public void setWorkingCapitalInitialization(WorkingCapitalInitialization workingCapitalInitialization) {
        this.workingCapitalInitialization = workingCapitalInitialization;
    }

    @Override
    public boolean hasAllneededInfo() {
	return getWorkingCapitalInitialization() != null;
    }

}
