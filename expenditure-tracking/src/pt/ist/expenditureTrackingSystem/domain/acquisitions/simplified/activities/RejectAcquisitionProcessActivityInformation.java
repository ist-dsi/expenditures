package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;

public class RejectAcquisitionProcessActivityInformation extends ActivityInformation<RegularAcquisitionProcess> {

    private String rejectionJustification;

    public RejectAcquisitionProcessActivityInformation(RegularAcquisitionProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    public String getRejectionJustification() {
	return rejectionJustification;
    }

    public void setRejectionJustification(String rejectionJustification) {
	this.rejectionJustification = rejectionJustification;
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && getRejectionJustification() != null;
    }
}
