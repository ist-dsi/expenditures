package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.presentationTier.actions.CommentBean;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.RoleType;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class ExceptionalChangeRequestingPerson extends
	WorkflowActivity<RegularAcquisitionProcess, ExceptionalChangeRequestingPersonInfo> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final RegularAcquisitionProcess process, final User user) {
	return user.hasRoleType(RoleType.MANAGER);
    }

    @Override
    protected void process(final ExceptionalChangeRequestingPersonInfo activityInformation) {
	PaymentProcess process = activityInformation.getProcess();
	process.getRequest().setRequester(activityInformation.getRequester().getUser().getExpenditurePerson());

	CommentBean commentBean = new CommentBean(process);
	commentBean.setComment(activityInformation.getComment());
	process.createComment(UserView.getCurrentUser(), commentBean);
    }

    @Override
    public ActivityInformation<RegularAcquisitionProcess> getActivityInformation(final RegularAcquisitionProcess process) {
	return new ExceptionalChangeRequestingPersonInfo(process, this);
    }

    @Override
    public String getUsedBundle() {
	return "resources/AcquisitionResources";
    }

    @Override
    public boolean isUserAwarenessNeeded(RegularAcquisitionProcess process) {
	return false;
    }

    @Override
    public boolean isConfirmationNeeded(RegularAcquisitionProcess process) {
	return true;
    }

    @Override
    protected String[] getArgumentsDescription(ExceptionalChangeRequestingPersonInfo activityInformation) {
	Person oldRequester = activityInformation.getProcess().getRequestor();
	return new String[] { (oldRequester == null) ? "-" : oldRequester.getUser().getPresentationName(),
		activityInformation.getRequester().getUser().getPresentationName() };
    }
}
