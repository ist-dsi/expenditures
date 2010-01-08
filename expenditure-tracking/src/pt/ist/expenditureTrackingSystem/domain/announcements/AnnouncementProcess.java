package pt.ist.expenditureTrackingSystem.domain.announcements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.workflow.domain.ActivityLog;
import myorg.domain.User;
import myorg.domain.exceptions.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.announcement.AnnouncementProcessState;
import pt.ist.expenditureTrackingSystem.domain.announcements.activities.ApproveAnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.activities.CancelAnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.activities.CloseAnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.activities.EditAnnouncementForApproval;
import pt.ist.expenditureTrackingSystem.domain.announcements.activities.RejectAnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.activities.SubmitAnnouncementForApproval;
import pt.ist.expenditureTrackingSystem.domain.dto.AnnouncementBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixWebFramework.services.Service;

public class AnnouncementProcess extends AnnouncementProcess_Base {

    private static ArrayList<AbstractActivity> activities = new ArrayList<AbstractActivity>();

    static {
	activities.add(new EditAnnouncementForApproval());
	activities.add(new SubmitAnnouncementForApproval());
	activities.add(new ApproveAnnouncementProcess());
	activities.add(new RejectAnnouncementProcess());
	activities.add(new CancelAnnouncementProcess());
	activities.add(new CloseAnnouncementProcess());
    }

    @Override
    public <T extends GenericProcess> AbstractActivity<T> getActivityByName(String activityName) {

	for (AbstractActivity activity : activities) {
	    if (activity.getName().equals(activityName)) {
		return activity;
	    }
	}
	return null;
    }

    public boolean isProcessInState(AnnouncementProcessStateType state) {
	return getAnnouncementProcessStateType().equals(state);
    }

    public AnnouncementProcessStateType getAnnouncementProcessState() {
	return getAnnouncementProcessStateType();
    }

    protected AnnouncementProcessState getLastAnnouncementProcessState() {
	return (AnnouncementProcessState) Collections.max(getProcessStates(), ProcessState.COMPARATOR_BY_WHEN);
    }

    public AnnouncementProcessStateType getAnnouncementProcessStateType() {
	return getLastAnnouncementProcessState().getAnnouncementProcessStateType();
    }

    public boolean isPersonAbleToExecuteActivities() {
	for (AbstractActivity<AnnouncementProcess> activity : activities) {
	    if (activity.isActive(this)) {
		return true;
	    }
	}
	return false;
    }

    @Service
    public static AnnouncementProcess createNewAnnouncementProcess(Person publisher, AnnouncementBean announcementBean) {
	if (!isCreateNewProcessAvailable()) {
	    throw new DomainException("announcementProcess.message.exception.invalidStateToRun.create");
	}

	announcementBean.setBuyingUnit(ExpenditureTrackingSystem.getInstance().getTopLevelUnitsSet().iterator().next());
	return new AnnouncementProcess(publisher, announcementBean);
    }

    private AnnouncementProcess(final Person publisher, AnnouncementBean announcementBean) {
	super();
	new AnnouncementProcessState(this, AnnouncementProcessStateType.IN_GENESIS);
	new Announcement(this, publisher, announcementBean);
    }

    public List<AbstractActivity> getActiveActivities() {
	List<AbstractActivity> activitiesResult = new ArrayList<AbstractActivity>();
	for (AbstractActivity activity : activities) {
	    if (activity.isActive(this)) {
		activitiesResult.add(activity);
	    }
	}
	return activitiesResult;
    }

    @Override
    public boolean hasAnyAvailableActivitity() {
	return !getActiveActivities().isEmpty();
    }

    public String getRejectionJustification() {
	if (getLastAnnouncementProcessState().getAnnouncementProcessStateType().equals(AnnouncementProcessStateType.REJECTED)) {
	    return getLastAnnouncementProcessState().getJustification();
	}
	return null;
    }

    public boolean isVisible(Person person) {
	return getLastAnnouncementProcessState().equals(AnnouncementProcessStateType.APPROVED)
		|| getAnnouncement().getPublisher() == person || person.hasRoleType(RoleType.ACQUISITION_CENTRAL)
		|| person.hasRoleType(RoleType.ACQUISITION_CENTRAL_MANAGER);
    }

    @Override
    public <T extends ActivityLog> T logExecution(User user, String operationName, String... args) {
	return (T) new OperationLog(this, user, operationName, getAnnouncementProcessStateType());
    }

    @Override
    public User getProcessCreator() {
	return getAnnouncement().getPublisher().getUser();
    }

    @Override
    public void notifyUserDueToComment(final User user, final String comment) {
	// do nothing.
    }
}
