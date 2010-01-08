package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class DeleteAcquisitionRequestItem extends
	WorkflowActivity<RegularAcquisitionProcess, DeleteAcquisitionRequestItemActivityInformation> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return process.getRequestor() == person && isUserProcessOwner(process, user)
		&& process.getAcquisitionProcessState().isInGenesis() && process.getAcquisitionRequest().hasAnyRequestItems();
    }

    @Override
    protected void process(DeleteAcquisitionRequestItemActivityInformation activityInformation) {
	activityInformation.getItem().delete();
    }

    @Override
    public ActivityInformation<RegularAcquisitionProcess> getActivityInformation(RegularAcquisitionProcess process) {
	return new DeleteAcquisitionRequestItemActivityInformation(process, this);
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

    @Override
    public boolean isConfirmationNeeded(RegularAcquisitionProcess process) {
	return true;
    }
}
