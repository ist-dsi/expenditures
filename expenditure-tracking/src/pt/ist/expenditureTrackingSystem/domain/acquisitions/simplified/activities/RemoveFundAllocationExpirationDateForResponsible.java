package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem._development.ExternalIntegration;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.UnApprove;

public class RemoveFundAllocationExpirationDateForResponsible extends
	WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	return isUserProcessOwner(process, user)
		&& process.isResponsibleForUnit(user.getExpenditurePerson())
		&& ((!process.getShouldSkipSupplierFundAllocation() && process.getFundAllocationExpirationDate() != null) || (process
			.getShouldSkipSupplierFundAllocation() && process.isPendingFundAllocation()))
		&& !process.hasAnyAllocatedFunds();
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
	final RegularAcquisitionProcess process = activityInformation.getProcess();
	activityInformation.getProcess().removeFundAllocationExpirationDate();
	UnApprove<RegularAcquisitionProcess> unApprove = new UnApprove<RegularAcquisitionProcess>();
	unApprove.execute(unApprove.getActivityInformation(activityInformation.getProcess()));

	if (ExternalIntegration.ACTIVE) {
	    // TODO : only uncomment this line when we want to integrate with MGP
	    process.cancelFundAllocationRequest(false);
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
