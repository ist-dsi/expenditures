package module.workingCapital.presentationTier.action;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.workflow.presentationTier.actions.ProcessManagement;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalSystem;
import module.workingCapital.domain.WorkingCapitalTransaction;
import module.workingCapital.domain.util.AcquisitionClassificationBean;
import module.workingCapital.domain.util.WorkingCapitalInitializationBean;
import module.workingCapital.presentationTier.action.util.WorkingCapitalContext;
import myorg.domain.exceptions.DomainException;
import myorg.presentationTier.actions.ContextBaseAction;
import myorg.util.BundleUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/workingCapital")
public class WorkingCapitalAction extends ContextBaseAction {

    public ActionForward frontPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	WorkingCapitalContext workingCapitalContext = getRenderedObject("workingCapitalContext");
	if (workingCapitalContext == null) {
	    workingCapitalContext = new WorkingCapitalContext();
	}
	return frontPage(request, workingCapitalContext);
    }

    public ActionForward frontPage(final HttpServletRequest request, final WorkingCapitalContext workingCapitalContext) {
	request.setAttribute("workingCapitalContext", workingCapitalContext);
	return forward(request, "/workingCapital/frontPage.jsp");
    }

    public ActionForward search(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final WorkingCapitalContext workingCapitalContext = getRenderedObject("workingCapitalInitializationBean");
	final SortedSet<WorkingCapitalProcess> unitProcesses = workingCapitalContext.getWorkingCapitalSearchByUnit();
	if (unitProcesses.size() == 1) {
	    final WorkingCapitalProcess workingCapitalProcess = unitProcesses.first();
	    return ProcessManagement.forwardToProcess(workingCapitalProcess);
	} else {
	    request.setAttribute("unitProcesses", unitProcesses);
	    return frontPage(request, workingCapitalContext);
	}
    }

    public ActionForward configuration(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstance();
	request.setAttribute("workingCapitalSystem", workingCapitalSystem);
	return forward(request, "/workingCapital/configuration.jsp");
    }

    public ActionForward prepareCreateWorkingCapitalInitialization(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final WorkingCapitalInitializationBean workingCapitalInitializationBean = new WorkingCapitalInitializationBean();
	return prepareCreateWorkingCapitalInitialization(request, workingCapitalInitializationBean);
    }

    public ActionForward prepareCreateWorkingCapitalInitialization(final HttpServletRequest request,
	    final WorkingCapitalInitializationBean workingCapitalInitializationBean) {
	request.setAttribute("workingCapitalInitializationBean", workingCapitalInitializationBean);
	return forward(request, "/workingCapital/createWorkingCapitalInitialization.jsp");
    }

    public ActionForward createWorkingCapitalInitialization(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final WorkingCapitalInitializationBean workingCapitalInitializationBean = getRenderedObject();
	try {
	    final WorkingCapitalInitialization workingCapitalInitialization = workingCapitalInitializationBean.create();
	    final WorkingCapital workingCapital = workingCapitalInitialization.getWorkingCapital();
	    final WorkingCapitalProcess workingCapitalProcess = workingCapital.getWorkingCapitalProcess();
	    return viewWorkingCapital(request, workingCapitalProcess);
	} catch (final DomainException domainException) {
	    RenderUtils.invalidateViewState();
	    addLocalizedMessage(request, BundleUtil.getFormattedStringFromResourceBundle("resources.WorkingCapitalResources",
		    	domainException.getKey()));
	    return prepareCreateWorkingCapitalInitialization(request, workingCapitalInitializationBean);
	}
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

    public ActionForward configureAccountingUnit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstance();
	request.setAttribute("workingCapitalSystem", workingCapitalSystem);
	return forward(request, "/workingCapital/configureAccountingUnit.jsp");
    }

    public ActionForward configureManagementUnit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstance();
	request.setAttribute("workingCapitalSystem", workingCapitalSystem);
	return forward(request, "/workingCapital/configureManagementUnit.jsp");
    }

    public ActionForward prepareAddAcquisitionClassification(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionClassificationBean acquisitionClassificationBean = new AcquisitionClassificationBean();
	request.setAttribute("acquisitionClassificationBean", acquisitionClassificationBean);
	return forward(request, "/workingCapital/addAcquisitionClassification.jsp");
    }

    public ActionForward addAcquisitionClassification(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionClassificationBean acquisitionClassificationBean = getRenderedObject();
	acquisitionClassificationBean.create();
	return configuration(mapping, form, request, response);
    }

    public ActionForward viewWorkingCapitalTransaction(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final WorkingCapitalTransaction workingCapitalTransaction = getDomainObject(request, "workingCapitalTransactionOid");
	request.setAttribute("workingCapitalTransaction", workingCapitalTransaction);
	request.setAttribute("process", workingCapitalTransaction.getWorkingCapital().getWorkingCapitalProcess());
	request.setAttribute("viewWorkingCapitalTransaction", Boolean.TRUE);
	return forward(request, "/module/workingCapital/domain/WorkingCapitalProcess/workingCapitalTransaction.jsp");
    }

    public ActionForward viewAllCapitalInitializations(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final WorkingCapital workingCapital = getDomainObject(request, "workingCapitalOid");
	request.setAttribute("workingCapital", workingCapital);

	return forward(request, "/workingCapital/viewAllWorkingCapitalInitializations.jsp");
    }

}
