package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;

public class AllocateFundsActivityInformation extends WorkingCapitalInitializationInformation {

    private String fundAllocationId;

    public AllocateFundsActivityInformation(final WorkingCapitalProcess workingCapitalProcess,
	    final WorkflowActivity<WorkingCapitalProcess, ? extends ActivityInformation<WorkingCapitalProcess>> activity) {
	super(workingCapitalProcess, activity);
	final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();
	final WorkingCapitalInitialization workingCapitalInitialization = workingCapital.getWorkingCapitalInitialization();
	setWorkingCapitalInitialization(workingCapitalInitialization);
    }

    @Override
    public void setWorkingCapitalInitialization(final WorkingCapitalInitialization workingCapitalInitialization) {
        super.setWorkingCapitalInitialization(workingCapitalInitialization);
        if (workingCapitalInitialization != null) {
            fundAllocationId = workingCapitalInitialization.getFundAllocationId();
        }
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput()
		&& super.hasAllneededInfo()
		&& fundAllocationId != null;
    }

    public String getFundAllocationId() {
        return fundAllocationId;
    }

    public void setFundAllocationId(String fundAllocationId) {
        this.fundAllocationId = fundAllocationId;
    }

}
