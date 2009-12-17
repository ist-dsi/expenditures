package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class RemovePermanentProjectFunds<P extends PaymentProcess> extends WorkflowActivity<P, ActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
	return process.isProjectAccountingEmployee(user.getExpenditurePerson()) && isUserProcessOwner(process, user)
		&& process.isInvoiceConfirmed() && allItemsAreFilledWithRealValues(process)
		&& process.getRequest().isEveryItemFullyAttributeInRealValues()
		&& process.hasAllocatedFundsPermanentlyForAllProjectFinancers();
    }

    @Override
    protected void process(ActivityInformation<P> activityInformation) {
	activityInformation.getProcess().getRequest().resetPermanentProjectFundAllocationId(Person.getLoggedPerson());
    }

    private boolean allItemsAreFilledWithRealValues(final P process) {
	for (final RequestItem requestItem : process.getRequest().getRequestItemsSet()) {
	    if (!requestItem.isFilledWithRealValues()) {
		return false;
	    }
	}
	return true;
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
