package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

public class LockInvoiceReceiving extends
	WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	AcquisitionProcessState acquisitionProcessState = process.getAcquisitionProcessState();

	return isUserProcessOwner(process, user)
		&& ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user)
		&& acquisitionProcessState.isAcquisitionProcessed()
		&& !process.getRequest().getInvoices().isEmpty();
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
	activityInformation.getProcess().invoiceReceived();
    }

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
	return "resources/AcquisitionResources";
    }
}
