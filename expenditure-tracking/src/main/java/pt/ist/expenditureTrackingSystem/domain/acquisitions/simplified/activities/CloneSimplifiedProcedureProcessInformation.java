package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class CloneSimplifiedProcedureProcessInformation extends ActivityInformation<SimplifiedProcedureProcess> {

    private static final long serialVersionUID = 1L;

    private SimplifiedProcedureProcess newProcess;

    public CloneSimplifiedProcedureProcessInformation(final SimplifiedProcedureProcess process, final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public SimplifiedProcedureProcess getNewProcess() {
        return newProcess;
    }

    public void setNewProcess(final SimplifiedProcedureProcess newProcess) {
        this.newProcess = newProcess;
    }

}
