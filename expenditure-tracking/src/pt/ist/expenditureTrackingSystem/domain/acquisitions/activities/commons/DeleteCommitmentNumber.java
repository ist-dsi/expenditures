package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class DeleteCommitmentNumber extends WorkflowActivity<RegularAcquisitionProcess, DeleteCommitmentNumberInformation> {

    @Override
    public boolean isActive(final RegularAcquisitionProcess process, final User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user)
		&& process.getAcquisitionProcessState().isAuthorized()
		&& process.hasCommitmentByUser(person);
    }

    @Override
    protected void process(final DeleteCommitmentNumberInformation activityInformation) {
	final Financer financer = activityInformation.getFinancer();
	if (financer != null) {
	    financer.setCommitmentNumber(null);
	}
    }

    @Override
    public ActivityInformation<RegularAcquisitionProcess> getActivityInformation(final RegularAcquisitionProcess process) {
        return new DeleteCommitmentNumberInformation(process, this);
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
    public boolean isUserAwarenessNeeded(RegularAcquisitionProcess process, User user) {
	return false;
    }

}
