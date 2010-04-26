package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.util.Money;

public class ReenforceWorkingCapitalInitializationActivityInformation extends ActivityInformation<WorkingCapitalProcess> {

    private Money amount = null;

    public ReenforceWorkingCapitalInitializationActivityInformation(WorkingCapitalProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
	return super.hasAllneededInfo() && isForwardedFromInput() && amount != null && amount.isPositive();
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

}
