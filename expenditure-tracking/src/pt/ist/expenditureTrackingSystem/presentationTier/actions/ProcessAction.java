package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.workflow.domain.WorkflowProcessComment;
import module.workflow.presentationTier.actions.CommentBean;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.ActivityException;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericFile;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.expenditureTrackingSystem.presentationTier.util.FileUploadBean;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public abstract class ProcessAction extends BaseAction {

    protected abstract <T extends GenericProcess> T getProcess(final HttpServletRequest request);

    protected GenericProcess getProcess(final HttpServletRequest request, final String attributeName) {
	final GenericProcess genericProcess = getDomainObject(request, attributeName);
	return genericProcess;
    }

    protected void genericActivityExecution(final GenericProcess genericProcess, final String activityName) {
	AbstractActivity<GenericProcess> acquitivity = genericProcess.getActivityByName(activityName);
	try {
	    acquitivity.execute(genericProcess);
	} catch (ActivityException e) {
	    addMessage(e.getMessage(), "EXPENDITURE_RESOURCES", e.getActivityName());
	}

    }

    protected void genericActivityExecution(final GenericProcess genericProcess, final String activityName, Object... args) {
	AbstractActivity<GenericProcess> acquitivity = genericProcess.getActivityByName(activityName);
	try {
	    acquitivity.execute(genericProcess, args);
	} catch (ActivityException e) {
	    addMessage(e.getMessage(), "EXPENDITURE_RESOURCES", e.getActivityName());
	}
    }

    protected void genericActivityExecution(final HttpServletRequest request, final String activityName) {
	final GenericProcess genericProcess = getProcess(request);
	genericActivityExecution(genericProcess, activityName);
    }

    protected void genericActivityExecution(final HttpServletRequest request, final String activityName, final Object... args) {
	final GenericProcess genericProcess = getProcess(request);
	genericActivityExecution(genericProcess, activityName, args);
    }

    protected String getBundle() {
	return StringUtils.EMPTY;
    }

    public abstract ActionForward viewProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response);

    public ActionForward downloadGenericFile(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws IOException {

	GenericFile file = getDomainObject(request, "fileOID");
	return download(response, file);
    }

    public ActionForward viewComments(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	GenericProcess process = getDomainObject(request, "processOid");
	request.setAttribute("process", process);

	Set<WorkflowProcessComment> comments = new TreeSet<WorkflowProcessComment>(WorkflowProcessComment.COMPARATOR);
	comments.addAll(process.getComments());

	process.markCommentsAsReadForUser(getLoggedPerson().getUser());

	request.setAttribute("comments", comments);
	request.setAttribute("bean", new CommentBean(process));

	return forward(request, "/acquisitions/viewComments.jsp");
    }

    public ActionForward addComment(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	CommentBean bean = getRenderedObject("comment");

	GenericProcess acquisitionProcess = getDomainObject(request, "processOid");
	acquisitionProcess.createComment(getLoggedPerson().getUser(), bean);

	RenderUtils.invalidateViewState();
	return viewComments(mapping, form, request, response);
    }

    public ActionForward prepareGenericUpload(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	GenericProcess process = getDomainObject(request, "processOid");
	FileUploadBean bean = new FileUploadBean();

	request.setAttribute("bean", bean);
	request.setAttribute("process", process);

	return forward(request, "/acquisitions/genericUpload.jsp");
    }

    public ActionForward genericUpload(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws IOException {
	FileUploadBean bean = getRenderedObject("uploadFile");
	GenericProcess process = getDomainObject(request, "processOid");
	process.addFile(bean.getDisplayName(), bean.getFilename(), consumeInputStream(bean));

	return viewProcess(mapping, form, request, response);

    }

    public ActionForward takeProcess(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	final GenericProcess process = getDomainObject(request, "processOid");

	if (!process.hasAnyAvailableActivitity() && request.getParameter("confirmTake") == null) {
	    request.setAttribute("confirmTake", "yes");
	    return viewProcess(mapping, form, request, response);
	}
	try {
	    process.takeProcess();
	} catch (DomainException e) {
	    addErrorMessage(e.getMessage(), getBundle());
	}

	return viewProcess(mapping, form, request, response);
    }

    public ActionForward releaseProcess(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	final GenericProcess process = getDomainObject(request, "processOid");
	try {
	    process.releaseProcess();
	} catch (DomainException e) {
	    addErrorMessage(e.getMessage(), getBundle());
	}

	return viewProcess(mapping, form, request, response);
    }

    public ActionForward stealProcess(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	final GenericProcess process = getDomainObject(request, "processOid");
	try {
	    process.stealProcess();
	} catch (DomainException e) {
	    addErrorMessage(e.getMessage(), getBundle());
	}

	return viewProcess(mapping, form, request, response);
    }

}
