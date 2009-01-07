package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.SearchRefundProcesses;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateRefundProcessBean;
import pt.ist.expenditureTrackingSystem.domain.dto.DomainObjectBean;
import pt.ist.expenditureTrackingSystem.domain.dto.RefundItemBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.ProcessAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/acquisitionRefundProcess")
@Forwards( { @Forward(name = "create.refund.process", path = "/acquisitions/refund/createRefundRequest.jsp"),
	@Forward(name = "view.refund.process", path = "/acquisitions/refund/viewRefundRequest.jsp"),
	@Forward(name = "search.refund.process", path = "/acquisitions/refund/searchRefundRequest.jsp"),
	@Forward(name = "create.refund.item", path = "/acquisitions/refund/createRefundItem.jsp"),
	@Forward(name = "edit.refund.item", path = "/acquisitions/refund/editRefundItem.jsp"),
	@Forward(name = "view.comments", path = "/acquisitions/viewComments.jsp"),
	@Forward(name = "generic.upload", path = "/acquisitions/genericUpload.jsp"),
	@Forward(name = "select.unit.to.add", path = "/acquisitions/refund/selectPayingUnitToAdd.jsp"),
	@Forward(name = "remove.paying.units", path = "/acquisitions/refund/removePayingUnits.jsp")
})
public class RefundProcessAction extends ProcessAction {

    private static final Context CONTEXT = new Context("acquisitions");

    @Override
    protected Context getContextModule(final HttpServletRequest request) {
	return CONTEXT;
    }

    public ActionForward viewRefundProcess(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	RefundProcess process = getProcess(request);
	return viewRefundProcess(mapping, request, process);
    }

    public ActionForward viewRefundProcess(final ActionMapping mapping, final HttpServletRequest request, RefundProcess process) {

	request.setAttribute("refundProcess", process);
	return mapping.findForward("view.refund.process");
    }

    public ActionForward prepareCreateRefundProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	CreateRefundProcessBean bean = new CreateRefundProcessBean(getLoggedPerson());
	request.setAttribute("bean", bean);
	return mapping.findForward("create.refund.process");
    }

    public ActionForward createRefundProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	CreateRefundProcessBean bean = getRenderedObject("createRefundProcess");
	RefundProcess process = RefundProcess.createNewRefundProcess(bean);
	return viewRefundProcess(mapping, request, process);

    }

    public ActionForward showPendingProcesses(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final SearchRefundProcesses searchRefundProcess = new SearchRefundProcesses();
	return searchRefundProcess(mapping, request, searchRefundProcess);
    }

    public ActionForward showMyProcesses(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final SearchRefundProcesses searchRefundProcess = new SearchRefundProcesses();
	searchRefundProcess.setRequestingPerson(getLoggedPerson());
	return searchRefundProcess(mapping, request, searchRefundProcess);
    }

    public ActionForward searchRefundProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	SearchRefundProcesses searchRefundProcess = getRenderedObject();
	if (searchRefundProcess == null) {
	    searchRefundProcess = new SearchRefundProcesses();
	}
	return searchRefundProcess(mapping, request, searchRefundProcess);
    }

    public ActionForward searchRefundProcess(final ActionMapping mapping, final HttpServletRequest request,
	    final SearchRefundProcesses searchRefundProcess) {
	request.setAttribute("searchRefundProcess", searchRefundProcess.search());
	return mapping.findForward("search.refund.process");
    }

    public ActionForward executeCreateRefundItem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	RefundItemBean bean = new RefundItemBean();
	request.setAttribute("bean", bean);
	request.setAttribute("refundProcess", getProcess(request));

	return mapping.findForward("create.refund.item");

    }

    public ActionForward actualCreationRefundItem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	RefundItemBean bean = getRenderedObject("refundItemBean");
	RefundProcess process = getProcess(request);
	genericActivityExecution(process, "CreateRefundItem", bean);

	return viewRefundProcess(mapping, request, process);
    }

    public ActionForward executeEditRefundItem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	RefundItem item = getDomainObject(request, "refundItemOid");
	RefundProcess process = getProcess(request);
	RefundItemBean bean = new RefundItemBean(item);
	request.setAttribute("refundProcess", process);
	request.setAttribute("refundItem", item);
	request.setAttribute("bean", bean);
	return mapping.findForward("edit.refund.item");
    }

    public ActionForward actualEditRefundItem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	RefundItemBean bean = getRenderedObject("refundItemBean");
	RefundItem item = getDomainObject(request,"refundItemOid");
	RefundProcess process = getProcess(request);
	genericActivityExecution(request, "EditRefundItem", item, bean);
	return viewRefundProcess(mapping, request, process);
    }

    public ActionForward executeDeleteRefundItem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	RefundItem item = getDomainObject(request, "refundItemOid");
	RefundProcess process = getProcess(request);
	genericActivityExecution(process, "DeleteRefundItem", item);
	return viewRefundProcess(mapping, request, process);
    }
    
    public ActionForward executeAddPayingUnit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final RefundProcess process = getProcess(request);
	request.setAttribute("refundProcess", process);
	request.setAttribute("domainObjectBean", new DomainObjectBean<Unit>());
	return mapping.findForward("select.unit.to.add");
    }

    public ActionForward addPayingUnit(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	DomainObjectBean<Unit> bean = getRenderedObject("unitToAdd");
	List<Unit> units = new ArrayList<Unit>();
	units.add(bean.getDomainObject());
	RefundProcess process = getProcess(request);
	genericActivityExecution(process, "AddPayingUnit",units);
	return viewRefundProcess(mapping, request, process);
    }

    public ActionForward executeRemovePayingUnit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final RefundProcess refundProcess = getProcess(request);
	request.setAttribute("refundProcess", refundProcess);
	request.setAttribute("payingUnits", refundProcess.getPayingUnits());
	return mapping.findForward("remove.paying.units");
    }

    public ActionForward removePayingUnit(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	final Unit payingUnit = getDomainObject(request, "unitOID");
	List<Unit> units = new ArrayList<Unit>();
	units.add(payingUnit);
	try {
	    genericActivityExecution(request, "RemovePayingUnit", units);
	} catch (DomainException e) {
	    addErrorMessage(e.getMessage(), getBundle());
	}
	return executeRemovePayingUnit(mapping, form, request, response);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected RefundProcess getProcess(HttpServletRequest request) {
	return (RefundProcess) getProcess(request, "refundProcessOid");
    }

    @Override
    public ActionForward viewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	RefundProcess process = getProcess(request);
	if (process == null) {
	    process = getDomainObject(request, "processOid");
	}
	return viewRefundProcess(mapping, request, process);
    }

}
