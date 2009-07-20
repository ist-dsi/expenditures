package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.SearchRefundProcesses;
import pt.ist.expenditureTrackingSystem.domain.dto.ChangeFinancerAccountingUnitBean;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateRefundProcessBean;
import pt.ist.expenditureTrackingSystem.domain.dto.EditRefundInvoiceBean;
import pt.ist.expenditureTrackingSystem.domain.dto.RefundInvoiceBean;
import pt.ist.expenditureTrackingSystem.domain.dto.RefundItemBean;
import pt.ist.expenditureTrackingSystem.domain.dto.VariantBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/acquisitionRefundProcess")
public class RefundProcessAction extends PaymentProcessAction {

    @Override
    protected String getSelectUnitToAddForwardUrl() {
	return "/acquisitions/commons/selectPayingUnitToAdd.jsp";
    }

    @Override
    protected String getRemovePayingUnitsForwardUrl() {
	return "/acquisitions/commons/removePayingUnits.jsp";
    }

    @Override
    protected String getAssignUnitItemForwardUrl() {
	return "/acquisitions/commons/assignUnitItem.jsp";
    }

    @Override
    protected String getEditRealShareValuesForwardUrl() {
	return "/acquisitions/commons/assignUnitItemRealValues.jsp";
    }

    public ActionForward viewRefundProcess(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	RefundProcess process = getProcess(request);
	return viewRefundProcess(mapping, request, process);
    }

    public ActionForward viewRefundProcess(final ActionMapping mapping, final HttpServletRequest request, RefundProcess process) {
	request.setAttribute("refundProcess", process);
	return forward(request, "/acquisitions/refund/viewRefundRequest.jsp");
    }

    public ActionForward prepareCreateRefundProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	CreateRefundProcessBean bean = new CreateRefundProcessBean(getLoggedPerson());
	request.setAttribute("bean", bean);
	return forward(request, "/acquisitions/refund/createRefundRequest.jsp");
    }

    public ActionForward createRefundProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	CreateRefundProcessBean bean = getRenderedObject("createRefundProcess");
	RefundProcess process = RefundProcess.createNewRefundProcess(bean);
	return viewRefundProcess(mapping, request, process);

    }

    public ActionForward createRefundProcessPostBack(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	CreateRefundProcessBean bean = getRenderedObject("createRefundProcess");
	request.setAttribute("bean", bean);
	RenderUtils.invalidateViewState("createRefundProcess");
	return forward(request, "/acquisitions/refund/createRefundRequest.jsp");

    }

    public ActionForward showPendingProcesses(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final SearchRefundProcesses searchRefundProcess = new SearchRefundProcesses();
	searchRefundProcess.setHasAvailableAndAccessibleActivityForUser(Boolean.TRUE);
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
	request.setAttribute("searchRefundProcess", searchRefundProcess);
	return forward(request, "/acquisitions/refund/searchRefundRequest.jsp");
    }

    public ActionForward executeCreateRefundItem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	RefundItemBean bean = new RefundItemBean();
	request.setAttribute("bean", bean);
	request.setAttribute("refundProcess", getProcess(request));

	return forward(request, "/acquisitions/refund/createRefundItem.jsp");

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
	return forward(request, "/acquisitions/refund/editRefundItem.jsp");
    }

    public ActionForward actualEditRefundItem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	RefundItemBean bean = getRenderedObject("refundItemBean");
	RefundItem item = getDomainObject(request, "refundItemOid");
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

    @Override
    @SuppressWarnings("unchecked")
    protected RefundProcess getProcess(HttpServletRequest request) {
	RefundProcess process = getDomainObject(request, "refundProcessOid");
	if (process == null) {
	    process = getDomainObject(request, "processOid");
	}
	return process;
    }

    @Override
    public ActionForward viewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return viewRefundProcess(mapping, request, getProcess(request));
    }

    public ActionForward executeSubmitForApproval(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "SubmitForApproval");
    }

    public ActionForward executeUnSubmitForApproval(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "UnSubmitForApproval");
    }

    @Override
    protected RefundItem getRequestItem(HttpServletRequest request) {
	RefundItem item = getDomainObject(request, "refundItemOid");
	return item != null ? item : (RefundItem) super.getRequestItem(request);
    }

    public ActionForward executeApprove(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "Approve");
    }

    public ActionForward executeUnApprove(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "UnApprove");
    }

    public ActionForward executeUnSubmitForFundAllocation(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "UnSubmitForFundAllocation");
    }

    public ActionForward executeCreateRefundInvoice(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	RefundItem item = getRequestItem(request);
	RefundInvoiceBean bean = new RefundInvoiceBean();
	bean.setItem(item);

	request.setAttribute("bean", bean);
	return forward(request, "/acquisitions/refund/addRefundInvoice.jsp");
    }

    public ActionForward createRefundInvoice(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	RefundProcess process = getProcess(request);
	RefundInvoiceBean bean = getRenderedObject("bean");

	if (bean.getInputStream() == null) {
	    addMessage("refundItem.message.info.mustAddInvoiceFile", getBundle());
	    request.setAttribute("bean", bean);
	    RenderUtils.invalidateViewState("bean");
	    return forward(request, "/acquisitions/refund/addRefundInvoice.jsp");
	}

	byte[] fileBytes = consumeInputStream(bean);
	try {
	    genericActivityExecution(process, "CreateRefundInvoice", bean, fileBytes);
	} catch (DomainException e) {
	    addErrorMessage(e.getMessage(), getBundle());
	    request.setAttribute("bean", bean);
	    RenderUtils.invalidateViewState("bean");
	    return forward(request, "/acquisitions/refund/addRefundInvoice.jsp");
	}

	return viewProcess(mapping, form, request, response);
    }

    public ActionForward executeSubmitForInvoiceConfirmation(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "SubmitForInvoiceConfirmation");
    }

    public ActionForward executeConfirmInvoices(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "ConfirmInvoices");
    }

    public ActionForward executeUnconfirmInvoices(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "UnconfirmInvoices");
    }

    public ActionForward downloadInvoice(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws IOException {
	RefundInvoice invoice = getDomainObject(request, "invoiceOID");
	download(response, invoice.getFile());
	return null;
    }

    public ActionForward executeRemoveRefundInvoice(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	RequestItem item = getRequestItem(request);
	request.setAttribute("item", item);
	return forward(request, "/acquisitions/refund/removeRefundInvoice.jsp");
    }

    public ActionForward removeRefundInvoice(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	RefundInvoice invoice = getDomainObject(request, "invoiceOid");
	RefundProcess process = getProcess(request);
	genericActivityExecution(process, "RemoveRefundInvoice", invoice);
	return executeRemoveRefundInvoice(mapping, form, request, response);
    }

    public ActionForward executeEditRefundInvoice(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	RefundItem item = getRequestItem(request);
	request.setAttribute("item", item);

	List<EditRefundInvoiceBean> beans = new ArrayList<EditRefundInvoiceBean>();
	for (RefundInvoice invoice : item.getInvoices()) {
	    beans.add(new EditRefundInvoiceBean(invoice));
	}
	request.setAttribute("invoices", beans);
	return forward(request, "/acquisitions/refund/editRefundInvoice.jsp");
    }

    public ActionForward editRefundInvoice(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	List<EditRefundInvoiceBean> beans = getRenderedObject("invoiceBeans");
	RefundProcess process = getProcess(request);
	try {
	    genericActivityExecution(process, "EditRefundInvoice", beans);
	} catch (DomainException e) {
	    addErrorMessage(e.getMessage(), getBundle());
	    RefundItem item = getRequestItem(request);
	    request.setAttribute("item", item);
	    request.setAttribute("invoices", beans);
	    return forward(request, "/acquisitions/refund/editRefundInvoice.jsp");
	}
	return viewProcess(mapping, form, request, response);
    }

    public ActionForward invalidValueRefundInvoice(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	List<EditRefundInvoiceBean> beans = getRenderedObject("invoiceBeans");

	RenderUtils.invalidateViewState();
	addErrorMessage("message.info.mustFillAllFields", getBundle());
	RefundItem item = getRequestItem(request);
	request.setAttribute("item", item);
	request.setAttribute("invoices", beans);
	return forward(request, "/acquisitions/refund/editRefundInvoice.jsp");

    }

    public ActionForward executeRefundPerson(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final RefundProcess refundProcess = getProcess(request);
	VariantBean bean = new VariantBean();
	request.setAttribute("bean", bean);
	request.setAttribute("process", refundProcess);
	return forward(request, "/acquisitions/refund/executePayment.jsp");
    }

    public ActionForward executeRefundPersonAction(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	String paymentReference = getRenderedObject("reference");
	return executeActivityAndViewProcess(mapping, form, request, response, "RefundPerson", paymentReference);
    }

    public ActionForward executeCancelRefundProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	request.setAttribute("confirmCancelProcess", Boolean.TRUE);
	return viewProcess(mapping, form, request, response);
    }

    public ActionForward cancelRefundProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return executeActivityAndViewProcess(mapping, form, request, response, "CancelRefundProcess");
    }

    public ActionForward executeChangeFinancersAccountingUnit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final Person person = getLoggedPerson();
	RefundProcess process = getProcess(request);
	Set<Financer> financersWithFundsAllocated = process.getRequest().getAccountingUnitFinancerWithNoFundsAllocated(person);
	Set<ChangeFinancerAccountingUnitBean> financersBean = new HashSet<ChangeFinancerAccountingUnitBean>(
		financersWithFundsAllocated.size());
	for (Financer financer : financersWithFundsAllocated) {
	    financersBean.add(new ChangeFinancerAccountingUnitBean(financer, financer.getAccountingUnit()));
	}
	request.setAttribute("process", process);
	request.setAttribute("financersBean", financersBean);
	return forward(request, "/acquisitions/refund/changeFinancersAccountingUnit.jsp");
    }

    public ActionForward changeFinancersAccountingUnit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	Collection<ChangeFinancerAccountingUnitBean> financersBean = getRenderedObject();
	RefundProcess process = getDomainObject(request, "processOid");
	genericActivityExecution(process, "ChangeFinancersAccountingUnit", financersBean);
	return viewRefundProcess(mapping, request, process);
    }
}
