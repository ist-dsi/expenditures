package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;

public class PayAcquisitionActivityInformation extends ActivityInformation<RegularAcquisitionProcess> {

    String paymentReference;

    public PayAcquisitionActivityInformation(RegularAcquisitionProcess process,
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
	return isForwardedFromInput() && getPaymentReference() != null;
    }

}
