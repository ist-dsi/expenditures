package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import java.util.ArrayList;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.Announcement;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.requests.RequestForProposalProcess;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/expendituresHome")
public class HomeAction extends BaseAction {

    public final ActionForward firstPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws Exception {
	final User user = UserView.getUser();
	if (user != null) {
	    final Person person = user.getPerson();
	    final Set<AcquisitionProcess> pendingAuthorizationAcquisitionProcesses = person
		    .findAcquisitionProcessesPendingAuthorization();
	    request.setAttribute("pendingAuthorizationAcquisitionProcesses", pendingAuthorizationAcquisitionProcesses);
	} else {
	    return showAcquisitionAnnouncements(mapping, form, request, response);
	}
	request.setAttribute("user", user);
	return forward(request, "/hello.jsp");
    }

    public final ActionForward showActiveRequestsForProposal(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {

	ArrayList<RequestForProposalProcess> requests = new ArrayList<RequestForProposalProcess>();
	for (RequestForProposalProcess process : RequestForProposalProcess.getAllProcesses(RequestForProposalProcess.class)) {
	    if (process.hasNotExpired()) {
		requests.add(process);
	    }
	}
	request.setAttribute("activeRequests", requests);
	return forward(request, "public/viewRequestsForProposal.jsp");
    }
    
    public ActionForward viewRequestForProposalProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	
	request.setAttribute("requestForProposalProcess", getDomainObject(request, "requestForProposalProcessOid"));
	return mapping.findForward("view.request.process");
    }

    public final ActionForward showAcquisitionAnnouncements(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {

	ArrayList<Announcement> approvedList = new ArrayList<Announcement>();
	for (Announcement announcement : ExpenditureTrackingSystem.getInstance().getAnnouncements()) {
	    if (announcement.getAnnouncementProcess().isProcessInState(AnnouncementProcessStateType.APPROVED)) {
		approvedList.add(announcement);
	    }
	}
	request.setAttribute("announcements", approvedList);
	return forward(request, "public/viewAnnouncements.jsp");
    }

    
    public ActionForward viewAnnouncement(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	
	request.setAttribute("announcement", getDomainObject(request, "announcementOid"));
	return forward(request, "public/viewAnnouncement.jsp");
    }

}
