package pt.ist.expenditureTrackingSystem.domain.announcements;

import java.util.Collections;
import java.util.List;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.ActivityLog;
import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;
import myorg.domain.exceptions.DomainException;
import myorg.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.announcement.AnnouncementProcessState;
import pt.ist.expenditureTrackingSystem.domain.dto.AnnouncementBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.services.Service;

/*
 * TODO: This should be deleted
 */
@ClassNameBundle(bundle = "resources/ExpenditureResources", key = "label.process.announcements")
public class AnnouncementProcess extends AnnouncementProcess_Base {

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
	// new AnnouncementProcessState(this,
	// AnnouncementProcessStateType.IN_GENESIS);
	// new Announcement(this, publisher, announcementBean);
    }

    @Override
    public boolean hasAnyAvailableActivitity() {
	return false;
    }

    public String getRejectionJustification() {
	if (getLastAnnouncementProcessState().getAnnouncementProcessStateType().equals(AnnouncementProcessStateType.REJECTED)) {
	    return getLastAnnouncementProcessState().getJustification();
	}
	return null;
    }

    public boolean isVisible(Person person) {
	final User user = person == null ? null : person.getUser();
	return getLastAnnouncementProcessState().equals(AnnouncementProcessStateType.APPROVED)
		|| getAnnouncement().getPublisher() == person
		|| ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user)
		|| ExpenditureTrackingSystem.isAcquisitionCentralManagerGroupMember(user);
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
    public void notifyUserDueToComment(User user, String comment) {
	// no nothing
    }

    @Override
    public boolean isAccessible(User user) {
	return user == getProcessCreator();
    }

    /*
     * TODO: Implement this methods correctly
     */
    public <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> List<T> getActivities() {
	return Collections.EMPTY_LIST;
    }

    public boolean isActive() {
	return true;
    }
}
