package module.internalrequest.presentationTier.action;

import module.internalrequest.domain.InternalRequestProcess;
import module.internalrequest.domain.util.InternalRequestProcessCreationBean;
import module.workflow.presentationTier.actions.ProcessManagement;
import module.workflow.util.WorkflowProcessViewer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsApplication;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@StrutsApplication(bundle = "InternalRequestResources", path = "internalRequest",
        titleKey = "title.internalRequests",
        accessGroup = "logged", hint = "Expendutures")
@StrutsFunctionality(app = InternalRequestCreateProcessAction.class, path = "createInternalRequestProcess",
        titleKey = "link.new")
@Mapping(path = "/createInternalRequestProcess")
@WorkflowProcessViewer(value = { InternalRequestProcess.class })
public class InternalRequestCreateProcessAction extends BaseAction {

    @EntryPoint
    public ActionForward prepareNewInternalRequestCreation(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        InternalRequestProcessCreationBean internalRequestProcessCreationBean = getRenderedObject();
        if (internalRequestProcessCreationBean == null) {
            internalRequestProcessCreationBean = new InternalRequestProcessCreationBean();
        }
        request.setAttribute("internalRequestProcessCreationBean", internalRequestProcessCreationBean);
        return forward("/internalRequest/createNewInternalRequest.jsp");
    }

    public ActionForward newInternalRequestCreation(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final InternalRequestProcessCreationBean internalRequestProcessCreationBean = getRenderedObject();
        try {
            final InternalRequestProcess internalRequestProcess =
                    internalRequestProcessCreationBean.createNewInternalRequestProcess();
            request.setAttribute("internalRequestProcess", internalRequestProcess);
            return ProcessManagement.forwardToProcess(internalRequestProcess);
        } catch (final DomainException ex) {
            RenderUtils.invalidateViewState();
            addLocalizedMessage(request, ex.getLocalizedMessage());
            request.setAttribute("internalRequestProcessCreationBean", internalRequestProcessCreationBean);
            return forward("/internalRequest/createNewInternalRequest.jsp");
        }
    }

}
