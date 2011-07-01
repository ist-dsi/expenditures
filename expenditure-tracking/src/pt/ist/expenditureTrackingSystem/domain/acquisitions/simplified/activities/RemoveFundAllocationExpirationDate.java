package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem._development.ExternalIntegration;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class RemoveFundAllocationExpirationDate extends
	WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    private boolean checkActiveConditions(RegularAcquisitionProcess process) {
	return process.getAcquisitionProcessState().isInAllocatedToSupplierState();
    }

    private boolean checkCanceledConditions(RegularAcquisitionProcess process) {
	return process.getAcquisitionProcessState().isCanceled();
    }

    private boolean hasAnyAssociatedProject(final RegularAcquisitionProcess process) {
	for (final Financer financer : process.getAcquisitionRequest().getFinancersSet()) {
	    if (financer.isProjectFinancer()) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user)
		&& (checkActiveConditions(process) || checkCanceledConditions(process))
		&& !process.hasAnyAllocatedFunds()
		&& ((process.isAccountingEmployee(person) && !hasAnyAssociatedProject(process))
			|| process.isProjectAccountingEmployee(person)
			|| ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user))
		&& ((!process.getShouldSkipSupplierFundAllocation() && process.getFundAllocationExpirationDate() != null) || (process
			.getShouldSkipSupplierFundAllocation() && process.isPendingFundAllocation()));
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
	RegularAcquisitionProcess process = activityInformation.getProcess();
	process.removeFundAllocationExpirationDate();
	process.getRequest().unSubmitForFundsAllocation();
	if (!process.getAcquisitionProcessState().isCanceled()) {
	    process.submitForApproval();
	}

	if (ExternalIntegration.isActive()) {
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
}
