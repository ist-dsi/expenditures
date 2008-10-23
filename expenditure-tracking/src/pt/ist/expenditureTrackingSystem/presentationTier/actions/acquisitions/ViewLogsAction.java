package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.SimplifiedAcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/viewLogs")
@Forwards( { @Forward(name = "view.operation.logs", path = "/acquisitions/operationLog.jsp") })
public class ViewLogsAction extends BaseAction {

    @Override
    protected Context getContextModule(final HttpServletRequest request) {
	final String module = request.getParameter("module");
	return new Context(module);
    }

    public ActionForward viewOperationLog(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final RegularAcquisitionProcess process = getDomainObject(request, "acquisitionProcessOid");
	final String state = request.getParameter("state");
	final SimplifiedAcquisitionProcessStateType stateType = state != null ? SimplifiedAcquisitionProcessStateType.valueOf(state) : null;

	if (stateType == null) {
	    request.setAttribute("operationLogs", process.getExecutionLogsSet());
	} else {
	    request.setAttribute("operationLogs", process.getOperationLogsInState(stateType));
	}

	request.setAttribute("process", process);
	return mapping.findForward("view.operation.logs");
    }

}
