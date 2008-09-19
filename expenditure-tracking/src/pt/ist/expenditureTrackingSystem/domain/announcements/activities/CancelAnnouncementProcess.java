package pt.ist.expenditureTrackingSystem.domain.announcements.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.announcement.AnnouncementProcessState;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcessStateType;
import pt.ist.fenixWebFramework.security.UserView;

public class CancelAnnouncementProcess extends GenericAnnouncementProcessActivity {

    @Override
    protected boolean isAccessible(AnnouncementProcess process) {
	User user = UserView.getUser();
	return user != null && user.getPerson().equals(process.getAnnouncement().getPublisher());
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
