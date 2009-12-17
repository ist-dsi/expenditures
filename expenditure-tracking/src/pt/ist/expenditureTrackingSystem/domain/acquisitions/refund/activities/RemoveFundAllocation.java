package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class RemoveFundAllocation extends WorkflowActivity<RefundProcess, ActivityInformation<RefundProcess>> {

    @Override
    public boolean isActive(RefundProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return process.isAccountingEmployee(person) && isUserProcessOwner(process, user) && process.isInAllocatedToUnitState()
		&& process.hasAllocatedFundsForAllProjectFinancers() && process.hasAllFundAllocationId(person);
    }

    @Override
    protected void process(ActivityInformation<RefundProcess> activityInformation) {
	RefundProcess process = activityInformation.getProcess();
	process.getRequest().resetFundAllocationId(Person.getLoggedPerson());
	process.submitForFundAllocation();
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
