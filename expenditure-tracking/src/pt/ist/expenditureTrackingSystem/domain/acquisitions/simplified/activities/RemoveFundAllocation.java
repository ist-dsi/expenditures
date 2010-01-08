package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

public class RemoveFundAllocation extends
	WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	return isUserProcessOwner(process, user) && process.isAccountingEmployee(user.getExpenditurePerson())
		&& (checkActiveConditions(process) || checkCanceledConditions(process))
		&& process.getAcquisitionRequest().hasAnyFundAllocationId(user.getExpenditurePerson());
    }

    private boolean checkActiveConditions(RegularAcquisitionProcess process) {
	return process.getAcquisitionProcessState().isInAllocatedToUnitState();
    }

    private boolean checkCanceledConditions(RegularAcquisitionProcess process) {
	return process.getAcquisitionProcessState().isCanceled();
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
	RegularAcquisitionProcess process = activityInformation.getProcess();
	process.getAcquisitionRequest().resetFundAllocationId(UserView.getCurrentUser().getExpenditurePerson());
	if (!process.getAcquisitionProcessState().isCanceled()) {
	    process.allocateFundsToSupplier();
	} else {
	    RemoveFundAllocationExpirationDate removeFundAllocationExpirationDate = new RemoveFundAllocationExpirationDate();
	    if (!process.hasAnyAllocatedFunds() && removeFundAllocationExpirationDate.isActive(process)) {
		removeFundAllocationExpirationDate.execute(removeFundAllocationExpirationDate.getActivityInformation(process));
	    }
	}
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
    public boolean isUserAwarenessNeeded(RegularAcquisitionProcess process, User user) {
	return false;
    }
}
