package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AdvancePaymentRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class DeleteAdvancePaymentRequestActivityInformation extends ActivityInformation<SimplifiedProcedureProcess> {

    private AdvancePaymentRequest advancePaymentRequest;

    public DeleteAdvancePaymentRequestActivityInformation(SimplifiedProcedureProcess process,
            WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
        if (process.getAcquisitionRequest().getAdvancePaymentRequest() != null) {
            setAdvancePaymentRequest(advancePaymentRequest);
        }
    }

    public AdvancePaymentRequest getAdvancePaymentRequest() {
        return advancePaymentRequest;
    }

    public void setAdvancePaymentRequest(AdvancePaymentRequest advancePaymentRequest) {
        this.advancePaymentRequest = advancePaymentRequest;
    }

    @Override
    public boolean hasAllneededInfo() {
        return getAdvancePaymentRequest() != null;
    }

}
