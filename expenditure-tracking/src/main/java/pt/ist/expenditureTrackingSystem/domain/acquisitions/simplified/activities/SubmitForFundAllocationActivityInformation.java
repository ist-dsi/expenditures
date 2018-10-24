package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import com.google.common.base.Strings;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;

public class SubmitForFundAllocationActivityInformation extends ActivityInformation<RegularAcquisitionProcess> {

    private boolean acceptAndApprove;

    public SubmitForFundAllocationActivityInformation(RegularAcquisitionProcess process,
            WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
        setAcceptAndApprove(false);
    }

    public boolean getAcceptAndApprove() {
        return acceptAndApprove;
    }

    public void setAcceptAndApprove(boolean acceptAndApprove) {
        this.acceptAndApprove = acceptAndApprove;
    }

    @Override
    public boolean hasAllneededInfo() {
        if (getProcess() instanceof SimplifiedProcedureProcess
                && ((SimplifiedProcedureProcess) getProcess()).getProcessClassification().equals(ProcessClassification.RAPID)
                && (ExpenditureTrackingSystem.getInstance().getApprovalTextForRapidAcquisitions() != null
                        && !ExpenditureTrackingSystem.getInstance().getApprovalTextForRapidAcquisitions().isEmpty())) {
            return getAcceptAndApprove();
        }
        return true;
    }

}
