package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProposalDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.SearchAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/acquisitionProcess")
@Forwards( { @Forward(name = "edit.request.acquisition", path = "/acquisitions/editAcquisitionRequest.jsp"),
	@Forward(name = "view.acquisition.process", path = "/acquisitions/viewAcquisitionProcess.jsp"),
	@Forward(name = "search.acquisition.process", path = "/acquisitions/searchAcquisitionProcess.jsp"),
	@Forward(name = "add.acquisition.proposal.document", path = "/acquisitions/addAcquisitionProposalDocument.jsp"),
	@Forward(name = "view.acquisition.request.item", path = "/acquisitions/viewAcquisitionRequestItem.jsp"),
	@Forward(name = "edit.acquisition.request.item", path = "/acquisitions/editAcquisitionRequestItem.jsp"),
	@Forward(name = "allocate.funds", path = "/acquisitions/allocateFunds.jsp"),
	@Forward(name = "allocate.funds.to.service.provider", path = "/acquisitions/allocateFundsToServiceProvider.jsp"),
	@Forward(name = "prepare.create.acquisition.request", path = "/acquisitions/createAcquisitionRequest.jsp"),
	@Forward(name = "view.active.processes", path = "/acquisitions/viewActiveProcesses.jsp")})
public class AcquisitionProcessAction extends BaseAction {

    private static final Context CONTEXT = new Context("acquisitions");

    @Override
    protected Context getContextModule() {
	return CONTEXT;
    }

    public static class AcquisitionProposalDocumentForm implements Serializable {
	private transient InputStream inputStream;
	private String filename;

	public AcquisitionProposalDocumentForm() {
	}

	public InputStream getInputStream() {
	    return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
	    this.inputStream = inputStream;
	}

	public String getFilename() {
	    return filename;
	}

	public void setFilename(String filename) {
	    this.filename = filename;
	}
    }

    public final ActionForward createNewAcquisitionProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = AcquisitionProcess.createNewAcquisitionProcess();
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	return editAcquisitionRequest(mapping, request, acquisitionProcess);
    }

    public final ActionForward editAcquisitionRequest(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	return editAcquisitionRequest(mapping, request, acquisitionProcess);
    }

    protected final ActionForward editAcquisitionRequest(final ActionMapping mapping, final HttpServletRequest request,
	    final AcquisitionProcess acquisitionProcess) {
	final AcquisitionRequest acquisitionRequest = acquisitionProcess.getAcquisitionRequest();
	request.setAttribute("acquisitionRequest", acquisitionRequest);
	return mapping.findForward("edit.request.acquisition");
    }

    public final ActionForward viewAcquisitionProcess(final ActionMapping mapping, final HttpServletRequest request,
	    final AcquisitionProcess acquisitionProcess) {
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	return mapping.findForward("view.acquisition.process");
    }

    public final ActionForward viewAcquisitionProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	return viewAcquisitionProcess(mapping, request, acquisitionProcess);
    }

    public final ActionForward deleteAcquisitionProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	acquisitionProcess.delete();
	return searchAcquisitionProcess(mapping, form, request, response);
    }

    public final ActionForward searchAcquisitionProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	SearchAcquisitionProcess searchAcquisitionProcess = getRenderedObject();
	if (searchAcquisitionProcess == null) {
	    searchAcquisitionProcess = new SearchAcquisitionProcess();
	    final User user = UserView.getUser();
	    if (user != null) {
		searchAcquisitionProcess.setRequester(user.getUsername());
	    }
	    searchAcquisitionProcess.setAcquisitionProcessState(AcquisitionProcessStateType.IN_GENESIS);
	}
	request.setAttribute("searchAcquisitionProcess", searchAcquisitionProcess);
	return mapping.findForward("search.acquisition.process");
    }

    public final ActionForward prepareAddAcquisitionProposalDocument(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	final AcquisitionProposalDocumentForm acquisitionProposalDocumentForm = new AcquisitionProposalDocumentForm();
	request.setAttribute("acquisitionProposalDocumentForm", acquisitionProposalDocumentForm);
	return mapping.findForward("add.acquisition.proposal.document");
    }

    public final ActionForward addAcquisitionProposalDocument(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	final AcquisitionProposalDocumentForm acquisitionProposalDocumentForm = getRenderedObject();
	final String filename = acquisitionProposalDocumentForm.getFilename();
	final byte[] bytes = consumeInputStream(acquisitionProposalDocumentForm.getInputStream());
	acquisitionProcess.addAcquisitionProposalDocument(filename, bytes);
	return viewAcquisitionProcess(mapping, request, acquisitionProcess);
    }

    public final ActionForward downloadAcquisitionProposalDocument(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws IOException {
	final OutputStream outputStream = response.getOutputStream();
	final AcquisitionProposalDocument acquisitionProposalDocument = getDomainObject(request, "acquisitionProposalDocumentOid");
	if (acquisitionProposalDocument != null && acquisitionProposalDocument.getContent() != null) {
	    response.setContentType("application/unknown");
	    response.setHeader("Content-disposition", "attachment; filename=" + acquisitionProposalDocument.getFilename());
	    outputStream.write(acquisitionProposalDocument.getContent().getBytes());
	}
	outputStream.flush();
	outputStream.close();
	return null;
    }

    public final ActionForward createNewAcquisitionRequestItem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	final AcquisitionRequestItem acquisitionRequestItem = acquisitionProcess.createAcquisitionRequestItem();
	return editAcquisitionRequestItem(mapping, request, acquisitionRequestItem);
    }

    protected final ActionForward editAcquisitionRequestItem(final ActionMapping mapping, final HttpServletRequest request,
	    final AcquisitionRequestItem acquisitionRequestItem) {
	final AcquisitionProcess acquisitionProcess = acquisitionRequestItem.getAcquisitionRequest().getAcquisitionProcess();
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	request.setAttribute("acquisitionRequestItem", acquisitionRequestItem);
	return mapping.findForward("edit.acquisition.request.item");
    }

    public final ActionForward editAcquisitionRequestItem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionRequestItem acquisitionRequestItem = getDomainObject(request, "acquisitionRequestItemOid");
	return editAcquisitionRequestItem(mapping, request, acquisitionRequestItem);
    }

    public final ActionForward viewAcquisitionRequestItem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionRequestItem acquisitionRequestItem = getDomainObject(request, "acquisitionRequestItemOid");
	request.setAttribute("acquisitionRequestItem", acquisitionRequestItem);
	return mapping.findForward("view.acquisition.request.item");
    }

    public final ActionForward deleteAcquisitionRequestItem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionRequestItem acquisitionRequestItem = getDomainObject(request, "acquisitionRequestItemOid");
	final AcquisitionProcess acquisitionProcess = acquisitionRequestItem.getAcquisitionRequest().getAcquisitionProcess();
	acquisitionRequestItem.delete();
	return viewAcquisitionProcess(mapping, request, acquisitionProcess);
    }

    public final ActionForward submitForApproval(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	acquisitionProcess.submitForApproval();
	return viewAcquisitionProcess(mapping, request, acquisitionProcess);
    }

    public final ActionForward approve(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	acquisitionProcess.approve();
	return viewAcquisitionProcess(mapping, request, acquisitionProcess);
    }

    public final ActionForward allocateFunds(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	return mapping.findForward("allocate.funds");
    }

    public final ActionForward allocateFundsToServiceProvider(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	return mapping.findForward("allocate.funds.to.service.provider");
    }

    public final ActionForward prepareCreateAcquisitionRequest(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	return mapping.findForward("prepare.create.acquisition.request");
    }
    
    public final ActionForward showPendingProcesses(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	List<AcquisitionProcess> processes = new ArrayList<AcquisitionProcess>();
	
	for (AcquisitionProcess process : ExpenditureTrackingSystem.getInstance().getAcquisitionProcesses()) {
	    if (process.isPersonAbleToExecuteActivities()) {
		processes.add(process);
	    }
	}
	request.setAttribute("activeProcesses", processes);
	
	return mapping.findForward("view.active.processes");
    }    

}
