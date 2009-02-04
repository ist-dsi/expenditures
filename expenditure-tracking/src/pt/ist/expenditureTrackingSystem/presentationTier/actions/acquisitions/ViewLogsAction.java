package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/viewLogs")
public class ViewLogsAction extends BaseAction {

    public ActionForward viewOperationLog(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	final PaymentProcess process = getDomainObject(request, "processOid");
	final String state = request.getParameter("state");

	request.setAttribute("operationLogs", (state == null) ? process.getExecutionLogsSet() : process
		.getExecutionLogsForState(state));

	request.setAttribute("process", process);
	return forward(request, "/acquisitions/commons/operationLog.jsp");
    }

}
