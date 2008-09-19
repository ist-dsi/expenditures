package pt.ist.expenditureTrackingSystem.presentationTier.actions.announcements;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.announcements.Announcement;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateAnnouncementBean;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.ProcessAction;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/announcementProcess")
@Forwards( { @Forward(name = "view.active.processes", path = "/announcements/viewActiveProcesses.jsp"),
	@Forward(name = "create.annoucement", path = "announcements/createAnnouncement.jsp"),
	@Forward(name = "view.announcementProcess", path = "announcements/viewAnnouncementProcess.jsp") })
public class AnnouncementsProcessAction extends ProcessAction {

    private static final Context CONTEXT = new Context("announcements");

    @Override
    protected Context getContextModule(final HttpServletRequest request) {
	return CONTEXT;
    }

    @Override
    protected GenericProcess getProcess(final HttpServletRequest request) {
	return getProcess(request, "announcementProcessOid");
    }

    public ActionForward showPendingProcesses(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return mapping.findForward("view.active.processes");
    }

    public ActionForward prepareCreateAnnouncement(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	request.setAttribute("announcementBean", new CreateAnnouncementBean());
	return mapping.findForward("create.annoucement");
    }

    public ActionForward createNewAnnouncementProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	User user = UserView.getUser();
	AnnouncementProcess announcementProcess = AnnouncementProcess.createNewAnnouncementProcess(user.getPerson(),
		(CreateAnnouncementBean) getRenderedObject());
	request.setAttribute("announcementProcess", announcementProcess);
	return mapping.findForward("view.announcementProcess");
    }
}
