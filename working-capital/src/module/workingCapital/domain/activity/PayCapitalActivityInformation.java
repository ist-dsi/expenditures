package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalRequest;

public class PayCapitalActivityInformation extends ActivityInformation<WorkingCapitalProcess> {

    private WorkingCapitalRequest workingCapitalRequest;

    public PayCapitalActivityInformation(final WorkingCapitalProcess workingCapitalProcess,
	    final WorkflowActivity<WorkingCapitalProcess, ? extends ActivityInformation<WorkingCapitalProcess>> activity) {
	super(workingCapitalProcess, activity);
    }

    public WorkingCapitalRequest getWorkingCapitalRequest() {
        return workingCapitalRequest;
    }

    public void setWorkingCapitalRequest(WorkingCapitalRequest workingCapitalRequest) {
        this.workingCapitalRequest = workingCapitalRequest;
    }

    @Override
    public boolean hasAllneededInfo() {
	return getWorkingCapitalRequest() != null;
    }

}
