package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalTransaction;

public class WorkingCapitalTransactionInformation extends ActivityInformation<WorkingCapitalProcess> {

    private WorkingCapitalTransaction workingCapitalTransaction;

    public WorkingCapitalTransactionInformation(final WorkingCapitalProcess workingCapitalProcess,
	    final WorkflowActivity<WorkingCapitalProcess, ? extends ActivityInformation<WorkingCapitalProcess>> activity) {
	super(workingCapitalProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
	return workingCapitalTransaction != null;
    }

    public WorkingCapitalTransaction getWorkingCapitalTransaction() {
        return workingCapitalTransaction;
    }

    public void setWorkingCapitalTransaction(final WorkingCapitalTransaction workingCapitalTransaction) {
        this.workingCapitalTransaction = workingCapitalTransaction;
    }

}
