package pt.ist.expenditureTrackingSystem.domain.announcements.activities;

import pt.ist.expenditureTrackingSystem.domain.announcement.AnnouncementProcessState;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.dto.AnnouncementBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class EditAnnouncementForApproval extends GenericAnnouncementProcessActivity {

    @Override
    protected boolean isAccessible(AnnouncementProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && loggedPerson.equals(process.getAnnouncement().getPublisher());
    }

    @Override
    protected boolean isAvailable(AnnouncementProcess process) {
	return process.isProcessInState(AnnouncementProcessStateType.IN_GENESIS)
		|| process.isProcessInState(AnnouncementProcessStateType.REJECTED);
    }

    @Override
    protected void process(AnnouncementProcess process, Object... objects) {
	AnnouncementBean announcementBean = (AnnouncementBean) objects[0];
	announcementBean.getAnnouncement().edit(announcementBean);
	if (process.isProcessInState(AnnouncementProcessStateType.REJECTED)) {
	    new AnnouncementProcessState(announcementBean.getAnnouncement().getAnnouncementProcess(),
		    AnnouncementProcessStateType.IN_GENESIS);
	}
    }

}
