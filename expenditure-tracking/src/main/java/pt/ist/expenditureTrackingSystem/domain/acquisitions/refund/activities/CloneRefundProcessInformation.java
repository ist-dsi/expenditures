package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class CloneRefundProcessInformation extends ActivityInformation<RefundProcess> {

    private static final long serialVersionUID = 1L;

    private RefundProcess newProcess;

    public CloneRefundProcessInformation(final RefundProcess process, final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public RefundProcess getNewProcess() {
        return newProcess;
    }

    public void setNewProcess(final RefundProcess newProcess) {
        this.newProcess = newProcess;
    }

}
