package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;

public class RefundPersonActivityInformation extends ActivityInformation<RefundProcess> {

    private String paymentReference;

    public RefundPersonActivityInformation(RefundProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    public String getPaymentReference() {
	return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
	this.paymentReference = paymentReference;
    }

    @Override
    public boolean hasAllneededInfo() {
	String paymentReference = getPaymentReference();
	return paymentReference != null && paymentReference.length() > 0;
    }
}
