package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class RemoveProjectFundAllocation extends WorkflowActivity<RefundProcess, ActivityInformation<RefundProcess>> {

    @Override
    public boolean isActive(RefundProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return process.isProjectAccountingEmployee(person) && isUserProcessOwner(process, user)
		&& process.hasAllocatedFundsForAllProjectFinancers(person) && process.isPendingFundAllocation();
    }

    @Override
    protected void process(ActivityInformation<RefundProcess> activityInformation) {
	activityInformation.getProcess().getRequest().resetProjectFundAllocationId(Person.getLoggedPerson());
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
