package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import com.google.common.base.Strings;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Payment;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class SetAdvancePaymentCompensationNumberInformation extends EditAdvancePaymentInformation {

    private String compensationNumber;

    public SetAdvancePaymentCompensationNumberInformation(SimplifiedProcedureProcess process,
            WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public String getCompensationNumber() {
        return compensationNumber;
    }

    public void setCompensationNumber(String compensationNumber) {
        this.compensationNumber = compensationNumber;
    }

    public void setPayment(Payment payment) {
        super.setPayment(payment);
        setCompensationNumber(payment.getCompensationNumber());
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput() && super.hasAllneededInfo();
    }
}
