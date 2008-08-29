package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import java.util.ArrayList;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.requests.RequestForProposalProcess;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/home")
@Forwards( { @Forward(name = "page.hello", path = "/hello.jsp"),
	@Forward(name = "view.requests", path = "/viewRequestsForProposal.jsp") })
public class HomeAction extends BaseAction {

    public final ActionForward firstPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws Exception {
	final User user = UserView.getUser();
	if (user != null) {
	    final Person person = user.getPerson();
	    final Set<AcquisitionProcess> pendingAuthorizationAcquisitionProcesses = person
		    .findAcquisitionProcessesPendingAuthorization();
	    request.setAttribute("pendingAuthorizationAcquisitionProcesses", pendingAuthorizationAcquisitionProcesses);
	}
	request.setAttribute("user", user);
	return mapping.findForward("page.hello");
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
	return mapping.findForward("view.requests");
    }

}
