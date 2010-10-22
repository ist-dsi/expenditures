package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcessComment;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class RejectAcquisitionProcess extends
	WorkflowActivity<RegularAcquisitionProcess, RejectAcquisitionProcessActivityInformation> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user)
		&& process.isPendingApproval()
		&& !process.getAcquisitionRequest().hasBeenAuthorizedBy(person)
		&& process.isResponsibleForUnit(person);
    }

    @Override
    protected void process(RejectAcquisitionProcessActivityInformation activityInformation) {
	RegularAcquisitionProcess process = activityInformation.getProcess();
	new WorkflowProcessComment(process, UserView.getCurrentUser(), activityInformation.getRejectionJustification());
	process.reject();
    }

    @Override
    public ActivityInformation<RegularAcquisitionProcess> getActivityInformation(RegularAcquisitionProcess process) {
	return new RejectAcquisitionProcessActivityInformation(process, this);
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
    public boolean isUserAwarenessNeeded(RegularAcquisitionProcess process, User user) {
	return false;
    }

}
