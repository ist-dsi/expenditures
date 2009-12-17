package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundableInvoiceFile;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class EditRefundInvoice extends WorkflowActivity<RefundProcess, EditRefundInvoiceActivityInformation> {

    @Override
    public boolean isActive(RefundProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user)

		&& process.isAnyRefundInvoiceAvailable()
		&& ((person == process.getRequestor() && process.isInAuthorizedState()) || (process
			.isPendingInvoicesConfirmation() && ((person.hasRoleType(RoleType.ACCOUNTING_MANAGER) && !process
			.hasProjectsAsPayingUnits()) || (person.hasRoleType(RoleType.PROJECT_ACCOUNTING_MANAGER) && process
			.hasProjectsAsPayingUnits()))));
    }

    @Override
    public EditRefundInvoiceActivityInformation getActivityInformation(RefundProcess process) {
	return new EditRefundInvoiceActivityInformation(process, this);
    }

    @Override
    protected void process(EditRefundInvoiceActivityInformation activityInformation) {
	RefundableInvoiceFile invoice = activityInformation.getInvoice();
	invoice.resetValues();
	invoice.editValues(activityInformation.getValue(), activityInformation.getVatValue(), activityInformation
		.getRefundableValue());
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
