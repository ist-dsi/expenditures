package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

public class SelectSupplierActivityInformation extends ActivityInformation<RegularAcquisitionProcess> {

    private Supplier supplier;

    public SelectSupplierActivityInformation(RegularAcquisitionProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    public Supplier getSupplier() {
	return supplier;
    }

    public void setSupplier(Supplier supplier) {
	this.supplier = supplier;
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && getSupplier() != null;
    }

}
