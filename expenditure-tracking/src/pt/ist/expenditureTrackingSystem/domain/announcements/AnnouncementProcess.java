package pt.ist.expenditureTrackingSystem.domain.announcements;

import java.util.ArrayList;
import java.util.Collections;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.announcement.AnnouncementProcessState;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateAnnouncementBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixWebFramework.services.Service;

public class AnnouncementProcess extends AnnouncementProcess_Base {

    private static ArrayList<AbstractActivity> activities = new ArrayList<AbstractActivity>();

    static {
	// activities.add(new CreateAnnouncement());
	// activities.add(new EditAnnouncement());
	// activities.add(new SubmitForApproval());
	// activities.add(new ApproveAnnouncement());
	// activities.add(new RejectAnnouncement());
	// activities.add(new CloseAnnouncement());
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
	return getLastAnnouncementProcessStateType().equals(state);
    }

    public AnnouncementProcessStateType getAnnouncementProcessStateType() {
	return getLastAnnouncementProcessStateType();
    }

    protected AnnouncementProcessState getLastAnnouncementProcessState() {
	return (AnnouncementProcessState) Collections.max(getProcessStates(), ProcessState.COMPARATOR_BY_WHEN);
    }

    protected AnnouncementProcessStateType getLastAnnouncementProcessStateType() {
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
    

    // TODO remove this when using activity for creating announcement
    @Service
    public static AnnouncementProcess createNewAnnouncementProcess(Person publisher, CreateAnnouncementBean announcementBean) {
	if (!isCreateNewProcessAvailable()) {
	    throw new DomainException("announcementProcess.message.exception.invalidStateToRun.create");
	}
	
	announcementBean.setBuyingUnit(ExpenditureTrackingSystem.getInstance().getTopLevelUnitsSet().iterator().next());
	return new AnnouncementProcess(publisher, announcementBean);
    }
    
    private AnnouncementProcess(final Person publisher, CreateAnnouncementBean announcementBean) {
	super();
	new AnnouncementProcessState(this, AnnouncementProcessStateType.IN_GENESIS);
	new Announcement(this, publisher, announcementBean);
    }



}
