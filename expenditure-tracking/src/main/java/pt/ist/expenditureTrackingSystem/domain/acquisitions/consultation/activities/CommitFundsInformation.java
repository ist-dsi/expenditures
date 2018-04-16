package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;

public class CommitFundsInformation extends ActivityInformation<MultipleSupplierConsultationProcess> {

    private static final long serialVersionUID = 1L;

    private String fundCommitmentNumber;

    public CommitFundsInformation(final MultipleSupplierConsultationProcess process,
            final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public String getFundCommitmentNumber() {
        return fundCommitmentNumber;
    }

    public void setFundCommitmentNumber(String fundCommitmentNumber) {
        this.fundCommitmentNumber = fundCommitmentNumber;
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput() && getFundCommitmentNumber() != null && !getFundCommitmentNumber().isEmpty();
    }

}
