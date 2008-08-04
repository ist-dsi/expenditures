package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/viewLogs")
@Forwards( { @Forward(name = "view.operation.logs", path = "/acquisitions/operationLog.jsp") })
public class ViewLogsAction extends BaseAction {

    public ActionForward viewOperationLog(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	AcquisitionProcess process = getDomainObject(request, "acquisitionProcessOid");
	String state = request.getParameter("state");
	AcquisitionProcessStateType stateType = state != null ? AcquisitionProcessStateType.valueOf(state) : null;
    
	if (stateType != null) {
	    request.setAttribute("operationLogs",process.getOperationLogsInState(stateType));
	}
	
	request.setAttribute("process", process);
	return mapping.findForward("view.operation.logs");
    }

}
