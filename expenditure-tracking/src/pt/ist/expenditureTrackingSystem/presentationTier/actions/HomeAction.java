package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchPaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.Announcement;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.requests.RequestForProposalProcess;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(path = "/expendituresHome")
public class HomeAction extends BaseAction {

    private static final int REQUESTS_PER_PAGE = 25;

    public final ActionForward firstPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws Exception {
	final Person person = getLoggedPerson();
	if (person != null) {
	    final Set<AcquisitionProcess> pendingAuthorizationAcquisitionProcesses = person
		    .findAcquisitionProcessesPendingAuthorization();
	    request.setAttribute("pendingAuthorizationAcquisitionProcesses", pendingAuthorizationAcquisitionProcesses);
	} else {
	    return showAcquisitionAnnouncements(mapping, form, request, response);
	}
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
	return forward(request, "public/viewRequestProcess.jsp");
    }

    public final ActionForward showAcquisitionAnnouncements(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {

	ArrayList<Announcement> approvedList = new ArrayList<Announcement>();
	for (Announcement announcement : ExpenditureTrackingSystem.getInstance().getAnnouncements()) {
	    if (announcement.getAnnouncementProcess().isProcessInState(AnnouncementProcessStateType.APPROVED)) {
		approvedList.add(announcement);
	    }
	}

	final CollectionPager<Announcement> pager = new CollectionPager<Announcement>((Collection) approvedList,
		REQUESTS_PER_PAGE);

	request.setAttribute("collectionPager", pager);
	request.setAttribute("numberOfPages", Integer.valueOf(pager.getNumberOfPages()));

	final String pageParameter = request.getParameter("pageNumber");
	final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);
	request.setAttribute("pageNumber", page);
	request.setAttribute("announcements", pager.getPage(page));

	return forward(request, "public/viewAnnouncements.jsp");
    }

}
