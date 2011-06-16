package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem._development.ExternalIntegration;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class RemoveProjectFundAllocation extends
	WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user) && process.isProjectAccountingEmployee(person)
		&& (checkActiveConditions(process) || checkCanceledConditions(process))
		&& process.hasAllocatedFundsForAllProjectFinancers(person) && !process.hasAnyNonProjectFundAllocationId();
    }

    private boolean checkActiveConditions(RegularAcquisitionProcess process) {
	return process.getAcquisitionProcessState().isInAllocatedToSupplierState();
    }

    private boolean checkCanceledConditions(RegularAcquisitionProcess process) {
	return process.getAcquisitionProcessState().isCanceled();
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
	RegularAcquisitionProcess process = activityInformation.getProcess();
	process.getAcquisitionRequest().resetProjectFundAllocationId(UserView.getCurrentUser().getExpenditurePerson());
	RemoveFundAllocationExpirationDate removeFundAllocationExpirationDate = new RemoveFundAllocationExpirationDate();
	if (process.getAcquisitionProcessState().isCanceled() && !process.getAcquisitionRequest().hasAllFundAllocationId()
		&& removeFundAllocationExpirationDate.isActive(process)) {
	    removeFundAllocationExpirationDate.process(removeFundAllocationExpirationDate.getActivityInformation(process));
	}

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
