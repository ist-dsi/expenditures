package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundRequest;

public class SubmitForApproval extends WorkflowActivity<RefundProcess, ActivityInformation<RefundProcess>> {

    @Override
    public boolean isActive(RefundProcess process, User user) {
	final RefundRequest refundRequest = process.getRequest();

	return isUserProcessOwner(process, user) && process.getRequestor() == user.getExpenditurePerson()
		&& process.getProcessState().isInGenesis() && refundRequest.hasAnyRequestItems()
		&& refundRequest.isEveryItemFullyAttributedToPayingUnits();
    }

    @Override
    protected void process(ActivityInformation<RefundProcess> activityInformation) {
	activityInformation.getProcess().submitForApproval();
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
