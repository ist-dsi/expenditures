package pt.ist.expenditureTrackingSystem.presentationTier.actions.requests;

import java.io.IOException;
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
import pt.ist.expenditureTrackingSystem.domain.requests.RequestForProposalDocument;
import pt.ist.expenditureTrackingSystem.domain.requests.RequestForProposalProcess;
import pt.ist.expenditureTrackingSystem.domain.requests.SearchRequestProposal;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.ProcessAction;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/requestForProposalProcess")
public class RequestForProposalProcessAction extends ProcessAction {

    @Override
    public ActionForward viewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	GenericProcess process = getProcess(request);
	if (process == null) {
	    process = getDomainObject(request, "processOid");
	}
	return viewRequestForProposalProcess(mapping, request, (RequestForProposalProcess) process);
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
	return forward(request, "/requests/viewActiveRequests.jsp");
    }

    public ActionForward prepareCreateRequestForProposalProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final CreateRequestForProposalProcessBean requestBean = new CreateRequestForProposalProcessBean();
	request.setAttribute("requestForProposalBean", requestBean);
	return forward(request, "/requests/createRequestProcess.jsp");
    }

    public ActionForward createNewRequestForProposalProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	CreateRequestForProposalProcessBean requestBean = getRenderedObject();
	User user = UserView.getUser();
	requestBean.setRequester(user != null ? user.getPerson() : null);

	RequestForProposalProcess proposalProcess = RequestForProposalProcess.createNewRequestForProposalProcess(requestBean,
		consumeInputStream(requestBean));

	request.setAttribute("requestForProposalProcess", proposalProcess);
	return forward(request, "/requests/viewRequestProcess.jsp");
    }

    public ActionForward viewRequestForProposalProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final RequestForProposalProcess process = getDomainObject(request, "requestForProposalProcessOid");
	return viewRequestForProposalProcess(mapping, request, process);
    }

    public ActionForward viewRequestForProposalProcess(final ActionMapping mapping, final HttpServletRequest request,
	    final RequestForProposalProcess process) {
	request.setAttribute("requestForProposalProcess", process);
	return forward(request, "/requests/viewRequestProcess.jsp");
    }

    public ActionForward downloadRequestForProposalDocument(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws IOException  {
	RequestForProposalDocument document = getDomainObject(request, "requestForProposalDocumentOid");
	return download(response, document);
    }
    
    // -------------------------------------------------- PROCESS ACTIVITIES - PROCESS ACTIVITIES - PROCESS ACTIVITIES BEGIN
    protected ActionForward executeActivityAndViewProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response, final String activityName) {
	genericActivityExecution(request, activityName);
	return viewRequestForProposalProcess(mapping, request, (RequestForProposalProcess) getDomainObject(request,
	"requestForProposalProcessOid"));
    }

    public ActionForward executeSubmitRequestForApproval(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "SubmitRequestForApproval");
    }
    
    public ActionForward executeApproveRequestForProposal(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "ApproveRequestForProposal");
    }

    public ActionForward executeRejectRequestForProposal(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "RejectRequestForProposal");
    }

     public ActionForward executeCancelRequestForProposal(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "CancelRequestForProposal");
    }
    
    public ActionForward executeEditRequestForProposal(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final RequestForProposalProcess process = (RequestForProposalProcess) getProcess(request);
	final CreateRequestForProposalProcessBean requestBean = new CreateRequestForProposalProcessBean(process);
	request.setAttribute("requestForProposalProcessBean", requestBean);
	request.setAttribute("requestForProposalProcessOid", process.getOID());
	return forward(request, "/requests/editRequestProcess.jsp");
    }
    
    public ActionForward executeChooseSupplierProposal(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	request.setAttribute("requestForProposalProcessOid", getProcess(request).getOID());
	return forward(request, "/requests/chooseSupplierProposal.jsp");
    }
    
    public ActionForward chooseSupplierProposal(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	//TODO
	return forward(request, "/requests/chooseSupplierProposal.jsp");
    }
    
    public ActionForward editRequestForProposal(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final CreateRequestForProposalProcessBean requestBean = getRenderedObject();
	final byte[] bytes = consumeInputStream(requestBean);
	final RequestForProposalProcess process = (RequestForProposalProcess) getProcess(request);
	process.getActivityByName("EditRequestForProposal").execute(process, requestBean, bytes);
	return viewRequestForProposalProcess(mapping, request, process);
    }

    // ---------------------------------------------------- PROCESS ACTIVITIES - PROCESS ACTIVITIES - PROCESS ACTIVITIES END

    public ActionForward searchRequestProposalProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	
	SearchRequestProposal searchProposalProcess = getRenderedObject("searchProposalRequestProcess");
	if (searchProposalProcess == null) {
	    searchProposalProcess = new SearchRequestProposal();
	    searchProposalProcess.setRequestor(getLoggedPerson().getName());
	}
	request.setAttribute("searchRequestProposalProcess", searchProposalProcess);
	return forward(request, "/requests/searchProposalProcess.jsp");
    }

    @Override
    protected GenericProcess getProcess(final HttpServletRequest request) {
	return getProcess(request, "requestForProposalProcessOid");
    }
}
