package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class CommitFunds extends WorkflowActivity<MultipleSupplierConsultationProcess, CommitFundsInformation> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.PENDING_FUND_COMMITMENT
                && ExpenditureTrackingSystem.isFundCommitmentManagerGroupMember(user);
    }

    @Override
    protected void process(final CommitFundsInformation information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        process.getConsultation().setFundCommitmentNumber(information.getFundCommitmentNumber());
        process.setState(MultipleSupplierConsultationProcessState.PENDING_CANDIDATE_NOTIFICATION);
    }

    @Override
    public ActivityInformation<MultipleSupplierConsultationProcess> getActivityInformation(final MultipleSupplierConsultationProcess process) {
        return new CommitFundsInformation(process, this);
    }

    @Override
    public String getUsedBundle() {
        return "resources/ExpenditureResources";
    }

}
