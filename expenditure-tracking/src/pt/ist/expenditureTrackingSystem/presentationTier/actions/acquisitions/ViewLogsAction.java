package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/viewLogs")
@Forwards( { @Forward(name = "view.operation.logs", path = "/acquisitions/commons/operationLog.jsp") })
public class ViewLogsAction extends BaseAction {

    @Override
    protected Context getContextModule(final HttpServletRequest request) {
	final String module = request.getParameter("module");
	return new Context(module);
    }

    public ActionForward viewOperationLog(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	final PaymentProcess process = getDomainObject(request, "processOid");
	final String state = request.getParameter("state");

	request.setAttribute("operationLogs", (state == null) ? process.getExecutionLogsSet() : process
		.getExecutionLogsForState(state));

	request.setAttribute("process", process);
	return mapping.findForward("view.operation.logs");
    }

}
