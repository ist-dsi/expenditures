package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

public class DeleteAcquisitionRequestItemActivityInformation extends ActivityInformation<RegularAcquisitionProcess> {

    private AcquisitionRequestItem item;

    public DeleteAcquisitionRequestItemActivityInformation(RegularAcquisitionProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    public AcquisitionRequestItem getItem() {
	return item;
    }

    public void setItem(AcquisitionRequestItem item) {
	this.item = item;
    }

    @Override
    public boolean hasAllneededInfo() {
	return getItem() != null;
    }
   
}
