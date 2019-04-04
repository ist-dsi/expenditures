package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Payment;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class EditAdvancePaymentInformation extends RegisterAdvancePaymentInformation {

    private Payment payment;

    public EditAdvancePaymentInformation(SimplifiedProcedureProcess process,
            WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
        setReference(payment.getReference());
        setValue(payment.getValue());
        setAdditionalValue(payment.getAdditionalValue());
        setDate(payment.getDate());
        setDescription(payment.getDescription());
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput() && getPayment() != null && super.hasAllneededInfo();
    }
}
