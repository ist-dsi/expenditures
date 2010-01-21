package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class PayAcquisition extends WorkflowActivity<RegularAcquisitionProcess, PayAcquisitionActivityInformation> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user)
		&& process.getAcquisitionProcessState().isAllocatedPermanently()
		&& (person.hasRoleType(RoleType.TREASURY_MANAGER) || process.isTreasuryMember(person));
    }

    @Override
    protected void process(PayAcquisitionActivityInformation activityInformation) {
	RegularAcquisitionProcess process = activityInformation.getProcess();
	process.getAcquisitionRequest().setPaymentReference(activityInformation.getPaymentReference());
	process.acquisitionPayed();
    }

    @Override
    public ActivityInformation<RegularAcquisitionProcess> getActivityInformation(RegularAcquisitionProcess process) {
	return new PayAcquisitionActivityInformation(process, this);
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
