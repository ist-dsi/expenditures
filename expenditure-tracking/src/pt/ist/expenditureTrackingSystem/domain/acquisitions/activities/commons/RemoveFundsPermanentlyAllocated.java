package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;

public class RemoveFundsPermanentlyAllocated<P extends PaymentProcess> extends WorkflowActivity<P, ActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
	return process.isAccountingEmployee(user.getExpenditurePerson()) && isUserProcessOwner(process, user)
		&& process.isAllocatedPermanently() && !process.isPayed();
    }

    @Override
    protected void process(ActivityInformation<P> activityInformation) {
	activityInformation.getProcess().resetEffectiveFundAllocationId();

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
