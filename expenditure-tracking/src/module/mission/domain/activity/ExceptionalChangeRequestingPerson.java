package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.presentationTier.actions.CommentBean;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.RoleType;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class ExceptionalChangeRequestingPerson extends
	MissionProcessActivity<MissionProcess, ExceptionalChangeRequestingPersonInfo> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
	return user.hasRoleType(RoleType.MANAGER);
    }

    @Override
    protected void process(final ExceptionalChangeRequestingPersonInfo activityInformation) {
	MissionProcess process = activityInformation.getProcess();
	process.getMission().setRequestingPerson(activityInformation.getRequester());

	CommentBean commentBean = new CommentBean(process);
	commentBean.setComment(activityInformation.getComment());
	process.createComment(UserView.getCurrentUser(), commentBean);
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
	return new ExceptionalChangeRequestingPersonInfo(process, this);
    }

    @Override
    public String getUsedBundle() {
	return "resources/MissionResources";
    }

    @Override
    public boolean isUserAwarenessNeeded(MissionProcess process) {
	return false;
    }

    @Override
    public boolean isConfirmationNeeded(MissionProcess process) {
	return true;
    }

    @Override
    protected String[] getArgumentsDescription(ExceptionalChangeRequestingPersonInfo activityInformation) {
	Person oldRequester = activityInformation.getProcess().getMission().getRequestingPerson();
	return new String[] { (oldRequester == null) ? "-" : oldRequester.getPresentationName(),
		activityInformation.getRequester().getUser().getPresentationName() };
    }
}
