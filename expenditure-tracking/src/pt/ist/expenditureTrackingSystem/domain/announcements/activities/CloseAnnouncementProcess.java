package pt.ist.expenditureTrackingSystem.domain.announcements.activities;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.announcement.AnnouncementProcessState;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcessStateType;

public class CloseAnnouncementProcess extends GenericAnnouncementProcessActivity {

    @Override
    protected boolean isAccessible(AnnouncementProcess process) {
	User user = getUser();
	return user != null && userHasRole(RoleType.ACQUISITION_CENTRAL_MANAGER);
    }

    @Override
    protected boolean isAvailable(AnnouncementProcess process) {
	return process.isProcessInState(AnnouncementProcessStateType.APPROVED);
    }

    @Override
    protected void process(AnnouncementProcess process, Object... objects) {
	process.getAnnouncement().setCreationDate(new DateTime());
	new AnnouncementProcessState(process, AnnouncementProcessStateType.CLOSED);
    }

}
