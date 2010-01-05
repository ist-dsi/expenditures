package module.workingCapital.presentationTier.action;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	request.setAttribute("workingCapitalProcess", workingCapitalProcess);
	return forward(request, "/workingCapital/workingCapitalProcess.jsp");
    }

    public ActionForward viewWorkingCapital(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final WorkingCapital workingCapital = getDomainObject(request, "workingCapitalOid");
	final WorkingCapitalProcess workingCapitalProcess = workingCapital.getWorkingCapitalProcess();
	return viewWorkingCapital(request, workingCapitalProcess);
    }

}
