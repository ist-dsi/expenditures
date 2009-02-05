package pt.ist.expenditureTrackingSystem.domain.announcements.activities;

import pt.ist.expenditureTrackingSystem.domain.announcement.AnnouncementProcessState;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class CancelAnnouncementProcess extends GenericAnnouncementProcessActivity {

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
	new AnnouncementProcessState(process, AnnouncementProcessStateType.CANCELED);
    }

}
