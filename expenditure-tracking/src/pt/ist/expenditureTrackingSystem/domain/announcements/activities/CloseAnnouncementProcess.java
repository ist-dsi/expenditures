package pt.ist.expenditureTrackingSystem.domain.announcements.activities;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.announcement.AnnouncementProcessState;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class CloseAnnouncementProcess extends GenericAnnouncementProcessActivity {

    @Override
    protected boolean isAccessible(AnnouncementProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && userHasRole(RoleType.ACQUISITION_CENTRAL_MANAGER);
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
