package module.workingCapital.presentationTier.action;

import java.util.Set;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.organization.domain.Accountability;
import module.workflow.presentationTier.actions.ProcessManagement;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalSystem;
import module.workingCapital.domain.util.WorkingCapitalInitializationBean;
import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/workingCapital")
public class WorkingCapitalAction extends ContextBaseAction {

    public ActionForward frontPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final Set<WorkingCapital> workingCapitals = WorkingCapitalSystem.getInstance().getWorkingCapitalsSet();
	request.setAttribute("workingCapitals", workingCapitals);
	return forward(request, "/workingCapital/frontPage.jsp");
    }

    public ActionForward configuration(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstance();
	request.setAttribute("workingCapitalSystem", workingCapitalSystem);
	return forward(request, "/workingCapital/configuration.jsp");
    }

    public ActionForward prepareCreateWorkingCapitalInitialization(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final WorkingCapitalInitializationBean workingCapitalInitializationBean = new WorkingCapitalInitializationBean();
	request.setAttribute("workingCapitalInitializationBean", workingCapitalInitializationBean);
	return forward(request, "/workingCapital/createWorkingCapitalInitialization.jsp");
    }

    public ActionForward createWorkingCapitalInitialization(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final WorkingCapitalInitializationBean workingCapitalInitializationBean = getRenderedObject();
	final WorkingCapitalProcess workingCapitalProcess = workingCapitalInitializationBean.create();
	return viewWorkingCapital(request, workingCapitalProcess);
    }

    public ActionForward viewWorkingCapital(final HttpServletRequest request, final WorkingCapitalProcess workingCapitalProcess) {
	return ProcessManagement.forwardToProcess(workingCapitalProcess);
    }

    public ActionForward viewWorkingCapital(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final WorkingCapital workingCapital = getDomainObject(request, "workingCapitalOid");
	final WorkingCapitalProcess workingCapitalProcess = workingCapital.getWorkingCapitalProcess();
	return viewWorkingCapital(request, workingCapitalProcess);
    }

    public ActionForward configureAccountingUnit(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstance();
	request.setAttribute("workingCapitalSystem", workingCapitalSystem);
	return forward(request, "/workingCapital/configureAccountingUnit.jsp");
    }

    public ActionForward configureManagementUnit(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstance();
	request.setAttribute("workingCapitalSystem", workingCapitalSystem);
	return forward(request, "/workingCapital/configureManagementUnit.jsp");
    }

}
