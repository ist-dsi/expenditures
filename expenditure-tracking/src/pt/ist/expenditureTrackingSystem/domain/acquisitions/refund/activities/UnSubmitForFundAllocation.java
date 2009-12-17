package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class UnSubmitForFundAllocation extends WorkflowActivity<RefundProcess, ActivityInformation<RefundProcess>> {

    @Override
    public boolean isActive(RefundProcess process, User user) {
	return (process.isAccountingEmployee(user.getExpenditurePerson()) || process.isProjectAccountingEmployee(user
		.getExpenditurePerson()))
		&& isProcessTakenByUser(process, user) && process.isPendingFundAllocation() && !process.hasAnyAllocatedFunds();
    }

    @Override
    protected void process(ActivityInformation<RefundProcess> activityInformation) {
	RefundProcess process = activityInformation.getProcess();
	process.unApproveByAll();
	process.submitForApproval();
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
