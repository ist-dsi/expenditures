package pt.ist.expenditureTrackingSystem.presentationTier.actions.requests;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateRequestForProposalProcessBean;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.expenditureTrackingSystem.domain.requests.RequestForProposalProcess;
import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.ProcessAction;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/requestForProposalProcess")
@Forwards( { @Forward(name = "view.active.requests", path = "/requests/viewActiveRequests.jsp"),
	@Forward(name = "create.request.process", path = "/requests/createRequestProcess.jsp"),
	@Forward(name = "view.request.process", path = "/requests/viewRequestProcess.jsp") })
public class RequestForProposalProcessAction extends ProcessAction {

    private static final Context CONTEXT = new Context("requests");

    @Override
    protected Context getContextModule() {
	return CONTEXT;
    }

    public ActionForward showPendingRequests(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	List<RequestForProposalProcess> processes = new ArrayList<RequestForProposalProcess>();

	for (RequestForProposalProcess process : GenericProcess.getAllProcesses(RequestForProposalProcess.class)) {
	    if (process.isPersonAbleToExecuteActivities()) {
		processes.add((RequestForProposalProcess) process);
	    }
	}
	request.setAttribute("activeRequests", processes);
	return mapping.findForward("view.active.requests");
    }

    public ActionForward prepareCreateRequestForProposalProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final CreateRequestForProposalProcessBean requestBean = new CreateRequestForProposalProcessBean();
	request.setAttribute("requestForProposalBean", requestBean);
	return mapping.findForward("create.request.process");
    }

    public ActionForward createNewRequestForProposalProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	CreateRequestForProposalProcessBean requestBean = getRenderedObject();
	User user = UserView.getUser();
	requestBean.setRequester(user != null ? user.getPerson() : null);

	RequestForProposalProcess proposalProcess = RequestForProposalProcess.createNewRequestForProposalProcess(requestBean,
		consumeInputStream(requestBean));

	request.setAttribute("requestForProposalProcess", proposalProcess);
	return mapping.findForward("view.request.process");
    }

    public ActionForward viewRequestForProposalProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final RequestForProposalProcess process = getDomainObject(request, "requestForProposalProcessOid");
	return viewRequestForProposalProcess(mapping, request, process);
    }

    public ActionForward viewRequestForProposalProcess(final ActionMapping mapping, final HttpServletRequest request,
	    final RequestForProposalProcess process) {
	request.setAttribute("requestForProposalProcess", process);
	return mapping.findForward("view.request.process");
    }

    // -------------------------------------------------- PROCESS ACTIVITIES - PROCESS ACTIVITIES - PROCESS ACTIVITIES BEGIN
    public ActionForward executeApproveRequestForProposal(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "ApproveRequestForProposal");
    }

    public ActionForward executeRejectRequestForProposal(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "RejectRequestForProposal");
    }

    public ActionForward executeActivityAndViewProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response, final String activityName) {
	genericActivityExecution(request, activityName);
	return viewRequestForProposalProcess(mapping, request, (RequestForProposalProcess) getDomainObject(request,
		"requestForProposalProcessOid"));
    }

    @Override
    protected void genericActivityExecution(final HttpServletRequest request, final String activityName) {
	final GenericProcess genericProcess = getProcess(request, "requestForProposalProcessOid");
	genericActivityExecution(genericProcess, activityName);
    }
    // ---------------------------------------------------- PROCESS ACTIVITIES - PROCESS ACTIVITIES - PROCESS ACTIVITIES END
}
