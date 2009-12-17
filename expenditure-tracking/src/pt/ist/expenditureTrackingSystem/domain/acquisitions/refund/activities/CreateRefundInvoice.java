package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class CreateRefundInvoice extends WorkflowActivity<RefundProcess, CreateRefundInvoiceActivityInformation> {

    @Override
    public boolean isActive(RefundProcess process, User user) {
	return process.getRequestor() == user.getExpenditurePerson() && isUserProcessOwner(process, user)
		&& process.isInAuthorizedState();
    }

    @Override
    protected void process(CreateRefundInvoiceActivityInformation activityInformation) {
	activityInformation.getItem().createRefundInvoice(activityInformation.getInvoiceNumber(),
		activityInformation.getInvoiceDate(), activityInformation.getValue(), activityInformation.getVatValue(),
		activityInformation.getRefundableValue(), activityInformation.getBytes(), activityInformation.getFilename(),
		activityInformation.getSupplier());

    }

    @Override
    public ActivityInformation<RefundProcess> getActivityInformation(RefundProcess process) {
	return new CreateRefundInvoiceActivityInformation(process, this);
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
    public boolean isVisible() {
	return false;
    }
}
