package pt.ist.expenditureTrackingSystem.domain.announcements.activities;

import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public abstract class GenericAnnouncementProcessActivity extends AbstractActivity<AnnouncementProcess> {

    @Override
    public String getLocalizedName() {
	return RenderUtils.getResourceString("ANNOUCEMENT_RESOURCES", "label." + getClass().getName());
    }
}
