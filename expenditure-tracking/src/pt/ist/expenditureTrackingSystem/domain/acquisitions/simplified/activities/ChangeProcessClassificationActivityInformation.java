package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;

public class ChangeProcessClassificationActivityInformation extends ActivityInformation<SimplifiedProcedureProcess> {

    private ProcessClassification classification;

    public ChangeProcessClassificationActivityInformation(SimplifiedProcedureProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    public ProcessClassification getClassification() {
	return classification;
    }

    public void setClassification(ProcessClassification classification) {
	this.classification = classification;
    }

    @Override
    public void setProcess(SimplifiedProcedureProcess process) {
	super.setProcess(process);
	setClassification(process.getProcessClassification());
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && getClassification() != null;
    }
}
