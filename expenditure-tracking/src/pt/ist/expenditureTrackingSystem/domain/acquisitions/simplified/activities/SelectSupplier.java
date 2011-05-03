package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;

public class SelectSupplier extends WorkflowActivity<RegularAcquisitionProcess, SelectSupplierActivityInformation> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	return ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user)
		&& isUserProcessOwner(process, user)
		&& process.isSimplifiedAcquisitionProcess()
		&& process.getAcquisitionProcessState().isAuthorized()
		&& ((SimplifiedProcedureProcess) process).getProcessClassification() == ProcessClassification.CT75000;
    }

    @Override
    public ActivityInformation<RegularAcquisitionProcess> getActivityInformation(RegularAcquisitionProcess process) {
	return new SelectSupplierActivityInformation(process, this);
    }

    @Override
    protected void process(SelectSupplierActivityInformation activityInformation) {
	activityInformation.getProcess().getRequest().setSelectedSupplier(activityInformation.getSupplier());
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
