package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class EditRefundItem extends WorkflowActivity<RefundProcess, EditRefundItemActivityInformation> {

    @Override
    public boolean isActive(RefundProcess process, User user) {
	return process.getRequestor() == user.getExpenditurePerson() && isUserProcessOwner(process, user)
		&& process.isInGenesis();
    }

    @Override
    protected void process(EditRefundItemActivityInformation activityInformation) {
	activityInformation.getItem().edit(activityInformation.getValueEstimation(), activityInformation.getCPVReference(),
		activityInformation.getClassification(), activityInformation.getDescription());
    }

    @Override
    public ActivityInformation<RefundProcess> getActivityInformation(RefundProcess process) {
	return new EditRefundItemActivityInformation(process, this);
    }

    @Override
    public boolean isVisible() {
	return false;
    }

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
	return "resources/AcquisitionResources";
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
	return false;
    }
}
