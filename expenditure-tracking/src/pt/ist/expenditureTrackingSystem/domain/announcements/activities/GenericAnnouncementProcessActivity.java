package pt.ist.expenditureTrackingSystem.domain.announcements.activities;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.OperationLog;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public abstract class GenericAnnouncementProcessActivity extends AbstractActivity<AnnouncementProcess> {

    @Override
    protected void logExecution(AnnouncementProcess process, String operationName, User user) {
	new OperationLog(process, user.getPerson(), operationName, process.getAnnouncementProcessStateType(), new DateTime());
    }

    @Override
    public String getLocalizedName() {
	return RenderUtils.getResourceString("ANNOUCEMENT_RESOURCES", "label." + getClass().getName());
    }
}
