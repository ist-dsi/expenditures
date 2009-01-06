package pt.ist.expenditureTrackingSystem.presentationTier.actions.announcements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.SearchAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.announcements.SearchAnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.AnnouncementBean;
import pt.ist.expenditureTrackingSystem.domain.dto.ProcessStateBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.ProcessAction;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/announcementProcess")
@Forwards( { @Forward(name = "view.active.processes", path = "/announcements/viewActiveProcesses.jsp"),
	@Forward(name = "create.announcement", path = "announcements/createAnnouncement.jsp"),
	@Forward(name = "edit.announcement", path = "announcements/editAnnouncement.jsp"),
	@Forward(name = "view.announcement", path = "announcements/viewAnnouncement.jsp"),
	@Forward(name = "view.announcementProcess", path = "announcements/viewAnnouncementProcess.jsp"),
	@Forward(name = "search.announcement.process", path = "announcements/searchAnnouncementProcess.jsp"),
	@Forward(name = "reject.announcement.process", path = "announcements/rejectAnnouncementProcess.jsp") })
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

    @Override
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
	return mapping.findForward("view.active.processes");
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
	return mapping.findForward("view.active.processes");
    }

    public ActionForward prepareCreateAnnouncement(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	request.setAttribute("announcementBean", new AnnouncementBean());
	return mapping.findForward("create.announcement");
    }

    public ActionForward createNewAnnouncementProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	User user = UserView.getUser();
	AnnouncementProcess announcementProcess = AnnouncementProcess.createNewAnnouncementProcess(user.getPerson(),
		(AnnouncementBean) getRenderedObject());
	request.setAttribute("announcementProcess", announcementProcess);
	return mapping.findForward("view.announcementProcess");
    }

    public ActionForward searchAnnouncementProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	SearchAnnouncementProcess searchAnnouncementProcess = getRenderedObject();
	if (searchAnnouncementProcess == null) {
	    searchAnnouncementProcess = new SearchAnnouncementProcess();
	}
	request.setAttribute("searchAnnouncementProcess", searchAnnouncementProcess);
	return mapping.findForward("search.announcement.process");
    }

    public ActionForward viewAnnouncementProcess(final ActionMapping mapping, final HttpServletRequest request,
	    final AnnouncementProcess process) {
	request.setAttribute("announcementProcess", process);
	if (process.getAnnouncementProcessStateType().equals(AnnouncementProcessStateType.REJECTED)) {
	    request.setAttribute("rejectionMotive", process.getRejectionJustification());
	}
	return mapping.findForward("view.announcementProcess");
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
	return mapping.findForward("edit.announcement");
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
	    addErrorMessage(e.getMessage(), getBundle());
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
	return mapping.findForward("reject.announcement.process");
    }

    public ActionForward rejectAnnouncementProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final ProcessStateBean stateBean = getRenderedObject();
	try {
	    genericActivityExecution(request, "RejectAnnouncementProcess", stateBean.getJustification());
	} catch (DomainException e) {
	    addErrorMessage(e.getMessage(), getBundle());
	}
	return viewAnnouncementProcess(mapping, form, request, response);
    }

    public ActionForward executeCloseAnnouncementProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return executeActivityAndViewProcess(mapping, form, request, response, "CloseAnnouncementProcess");
    }

    // ----------------------------------- ACTIVITIES END

    // public ActionForward executeRejectAcquisitionProcess(final ActionMapping
    // mapping, final ActionForm form,
    // final HttpServletRequest request, final HttpServletResponse response) {
    //
    // final SimplifiedProcedureProcess acquisitionProcess =
    // getDomainObject(request, "acquisitionProcessOid");
    // request.setAttribute("acquisitionProcess", acquisitionProcess);
    // request.setAttribute("stateBean", new ProcessStateBean());
    // return mapping.findForward("reject.acquisition.process");
    // }
    //
    // public ActionForward rejectAcquisitionProcess(final ActionMapping
    // mapping, final ActionForm form,
    // final HttpServletRequest request, final HttpServletResponse response) {
    //
    // final ProcessStateBean stateBean = getRenderedObject();
    // try {
    // genericActivityExecution(request, "RejectAcquisitionProcess",
    // stateBean.getJustification());
    // } catch (DomainException e) {
    // addErrorMessage(e.getMessage(), getBundle());
    // }
    // return viewAcquisitionProcess(mapping, request,
    // (SimplifiedProcedureProcess) getDomainObject(request,
    // "acquisitionProcessOid"));
    // }

}
