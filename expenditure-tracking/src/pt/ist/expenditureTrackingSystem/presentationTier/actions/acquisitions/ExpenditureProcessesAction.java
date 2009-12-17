package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.workflow.domain.WorkflowProcess;
import module.workflow.presentationTier.WorkflowLayoutContext;
import myorg.presentationTier.Context;
import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.AbstractFundAllocationActivityInformation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.AllocateProjectFundsPermanentlyActivityInformation;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/expenditureProcesses")
public class ExpenditureProcessesAction extends ContextBaseAction {

    public ActionForward addAllocationFund(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	return addAllocationFundGeneric(mapping, request, "activityBean", "/workflow/nonDefaultActivityInput.jsp");
    }

    public ActionForward removeAllocationFund(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return removeAllocationFundGeneric(mapping, request, "activityBean", "/workflow/nonDefaultActivityInput.jsp");
    }

    public ActionForward addAllocationFundForProject(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return addAllocationFundGeneric(mapping, request, "activityBean", "/workflow/nonDefaultActivityInput.jsp");
    }

    public ActionForward removeAllocationFundForProject(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return removeAllocationFundGeneric(mapping, request, "activityBean", "/workflow/nonDefaultActivityInput.jsp");
    }

    private ActionForward addAllocationFundGeneric(final ActionMapping mapping, final HttpServletRequest request,
	    String viewStateID, String forward) {

	AbstractFundAllocationActivityInformation<PaymentProcess> activityInformation = getRenderedObject(viewStateID);
	final PaymentProcess process = activityInformation.getProcess();
	request.setAttribute("process", process);
	request.setAttribute("information", activityInformation);

	List<FundAllocationBean> fundAllocationBeans = activityInformation.getBeans();
	Integer index = Integer.valueOf(request.getParameter("index"));

	Financer financer = getDomainObject(request, "financerOID");
	FundAllocationBean fundAllocationBean = new FundAllocationBean(financer);
	fundAllocationBean.setFundAllocationId(null);
	fundAllocationBean.setEffectiveFundAllocationId(null);
	fundAllocationBean.setAllowedToAddNewFund(false);

	fundAllocationBeans.add(index + 1, fundAllocationBean);
	RenderUtils.invalidateViewState();
	request.setAttribute("inputInterface", activityInformation.getActivity().getClass().getName().replace('.', '/') + ".jsp");
	return forward(request, forward);
    }

    private ActionForward removeAllocationFundGeneric(final ActionMapping mapping, final HttpServletRequest request,
	    String viewStateID, String forward) {
	AbstractFundAllocationActivityInformation<PaymentProcess> activityInformation = getRenderedObject(viewStateID);
	final PaymentProcess process = activityInformation.getProcess();
	request.setAttribute("process", process);
	request.setAttribute("information", activityInformation);

	List<FundAllocationBean> fundAllocationBeans = activityInformation.getBeans();
	int index = Integer.valueOf(request.getParameter("index")).intValue();

	fundAllocationBeans.remove(index);
	RenderUtils.invalidateViewState();
	request.setAttribute("inputInterface", activityInformation.getActivity().getClass().getName().replace('.', '/') + ".jsp");
	return forward(request, forward);
    }

    @Override
    public Context createContext(String contextPathString, HttpServletRequest request) {
	WorkflowProcess process = getProcess(request);
	WorkflowLayoutContext layout = process.getLayout();
	layout.setElements(contextPathString);
	return layout;
    }

    protected <T extends WorkflowProcess> T getProcess(HttpServletRequest request) {
	return (T) getDomainObject(request, "processId");
    }
}
