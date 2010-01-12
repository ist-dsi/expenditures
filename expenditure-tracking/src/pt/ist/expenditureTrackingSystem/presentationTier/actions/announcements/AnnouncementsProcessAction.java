package pt.ist.expenditureTrackingSystem.presentationTier.actions.announcements;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myorg.domain.exceptions.DomainException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.announcements.SearchAnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.AnnouncementBean;
import pt.ist.expenditureTrackingSystem.domain.dto.ProcessStateBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.ActivityException;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/announcementProcess")
public class AnnouncementsProcessAction extends BaseAction {

    protected void genericActivityExecution(final AnnouncementProcess process, final String activityName, Object... args) {
	AbstractActivity<AnnouncementProcess> acquitivity = process.getActivityByName(activityName);
	acquitivity.execute(process, args);
    }

    protected void genericActivityExecution(final HttpServletRequest request, final String activityName, final Object... args) {
	final AnnouncementProcess process = getProcess(request);
	try {
	    genericActivityExecution(process, activityName, args);
	} catch (ActivityException e) {
	    addMessage(request, e.getMessage());
	}
    }

    protected AnnouncementProcess getProcess(final HttpServletRequest request) {
	return getDomainObject(request, "announcementProcessOid");
    }

    public ActionForward viewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	GenericProcess process = getProcess(request);
	if (process == null) {
	    process = getDomainObject(request, "processOid");
	}
	return viewAnnouncementProcess(mapping, request, (AnnouncementProcess) process);
    }

    public ActionForward showMyProcesses(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	Person person = getLoggedPerson();
	request.setAttribute("activeProcesses", person.getAnnouncementProcesses());
	return forward(request, "/announcements/viewActiveProcesses.jsp");
    }

    public ActionForward showPendingProcesses(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	List<AnnouncementProcess> processes = new ArrayList<AnnouncementProcess>();

	for (AnnouncementProcess process : GenericProcess.getAllProcesses(AnnouncementProcess.class)) {
	    if (process.isPersonAbleToExecuteActivities()) {
		processes.add((AnnouncementProcess) process);
	    }
	}
	request.setAttribute("activeProcesses", processes);
	return forward(request, "/announcements/viewActiveProcesses.jsp");
    }

    public ActionForward prepareCreateAnnouncement(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	request.setAttribute("announcementBean", new AnnouncementBean());
	return forward(request, "/announcements/createAnnouncement.jsp");
    }

    public ActionForward createNewAnnouncementProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final Person person = getLoggedPerson();
	AnnouncementProcess announcementProcess = AnnouncementProcess.createNewAnnouncementProcess(person,
		(AnnouncementBean) getRenderedObject());
	request.setAttribute("announcementProcess", announcementProcess);
	return forward(request, "/announcements/viewAnnouncementProcess.jsp");
    }

    public ActionForward searchAnnouncementProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	SearchAnnouncementProcess searchAnnouncementProcess = getRenderedObject();
	if (searchAnnouncementProcess == null) {
	    searchAnnouncementProcess = new SearchAnnouncementProcess();
	}
	request.setAttribute("searchAnnouncementProcess", searchAnnouncementProcess);
	return forward(request, "/announcements/searchAnnouncementProcess.jsp");
    }

    public ActionForward viewAnnouncementProcess(final ActionMapping mapping, final HttpServletRequest request,
	    final AnnouncementProcess process) {
	request.setAttribute("announcementProcess", process);
	if (process.getAnnouncementProcessStateType().equals(AnnouncementProcessStateType.REJECTED)) {
	    request.setAttribute("rejectionMotive", process.getRejectionJustification());
	}
	return forward(request, "/announcements/viewAnnouncementProcess.jsp");
    }

    public ActionForward viewAnnouncementProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	AnnouncementProcess process = (AnnouncementProcess) getProcess(request);
	return viewAnnouncementProcess(mapping, request, process);
    }

    public ActionForward executeActivityAndViewProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response, final String activityName) {
	genericActivityExecution(request, activityName);
	return viewAnnouncementProcess(mapping, form, request, response);
    }

    public ActionForward executeActivityAndViewProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response, final String activityName, Object... args) {
	genericActivityExecution(request, activityName, args);
	return viewAnnouncementProcess(mapping, form, request, response);
    }

    // --------------------------------- ACTIVITIES BEGIN
    public ActionForward executeEditAnnouncementForApproval(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	AnnouncementProcess process = (AnnouncementProcess) getProcess(request);
	request.setAttribute("announcementBean", AnnouncementBean.create(process.getAnnouncement()));
	return forward(request, "/announcements/editAnnouncement.jsp");
    }

    public ActionForward editAnnouncementForApproval(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return executeActivityAndViewProcess(mapping, form, request, response, "EditAnnouncementForApproval",
		(AnnouncementBean) getRenderedObject());
    }

    public ActionForward executeCancelAnnouncementProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	try {
	    genericActivityExecution(request, "CancelAnnouncementProcess");
	} catch (DomainException e) {
	    addMessage(request, e.getMessage());
	}

	return showMyProcesses(mapping, form, request, response);
    }

    public ActionForward executeSubmitAnnouncementForApproval(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return executeActivityAndViewProcess(mapping, form, request, response, "SubmitAnnouncementForApproval");
    }

    public ActionForward executeApproveAnnouncementProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return executeActivityAndViewProcess(mapping, form, request, response, "ApproveAnnouncementProcess");
    }

    public ActionForward executeRejectAnnouncementProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	request.setAttribute("announcementProcess", getProcess(request));
	request.setAttribute("stateBean", new ProcessStateBean());
	return forward(request, "/announcements/rejectAnnouncementProcess.jsp");
    }

    public ActionForward rejectAnnouncementProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final ProcessStateBean stateBean = getRenderedObject();
	try {
	    genericActivityExecution(request, "RejectAnnouncementProcess", stateBean.getJustification());
	} catch (DomainException e) {
	    addMessage(request, e.getMessage());
	}
	return viewAnnouncementProcess(mapping, form, request, response);
    }

    public ActionForward executeCloseAnnouncementProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return executeActivityAndViewProcess(mapping, form, request, response, "CloseAnnouncementProcess");
    }

}
