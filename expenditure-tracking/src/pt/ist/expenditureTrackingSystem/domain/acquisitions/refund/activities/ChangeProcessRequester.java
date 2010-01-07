package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundRequest;

public class ChangeProcessRequester extends WorkflowActivity<RefundProcess, ChangeProcessRequesterActivityInformation> {

    @Override
    public boolean isActive(RefundProcess process, User user) {
	return process.isResponsibleForAtLeastOnePayingUnit(user.getExpenditurePerson());
    }

    @Override
    protected void process(ChangeProcessRequesterActivityInformation activityInformation) {
	final RefundProcess refundProcess = activityInformation.getProcess();
	final RefundRequest refundRequest = refundProcess.getRequest();
	refundRequest.setRequester(activityInformation.getPerson());
    }

    @Override
    public ActivityInformation<RefundProcess> getActivityInformation(RefundProcess process) {
	return new ChangeProcessRequesterActivityInformation(process, this);
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
    public boolean isUserAwarenessNeeded(RefundProcess process, User user) {
        return false;
    }

}