package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;
import pt.ist.expenditureTrackingSystem.domain.dto.DomainObjectBean;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;
import pt.ist.expenditureTrackingSystem.domain.dto.UnitItemBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.ProcessAction;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.UserView;

public abstract class PaymentProcessAction extends ProcessAction {

    public ActionForward executeGenericAddPayingUnit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final PaymentProcess process = getProcess(request);
	request.setAttribute("process", process);
	request.setAttribute("domainObjectBean", new DomainObjectBean<Unit>());
	return mapping.findForward("select.unit.to.add");
    }

    public ActionForward addPayingUnit(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	DomainObjectBean<Unit> bean = getRenderedObject("unitToAdd");
	List<Unit> units = new ArrayList<Unit>();
	units.add(bean.getDomainObject());
	genericActivityExecution(request, "GenericAddPayingUnit", units);
	return viewProcess(mapping, form, request, response);
    }

    public ActionForward executeGenericRemovePayingUnit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final PaymentProcess process = getProcess(request);
	request.setAttribute("process", process);
	request.setAttribute("payingUnits", process.getPayingUnits());
	return mapping.findForward("remove.paying.units");
    }

    public ActionForward removePayingUnit(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	final Unit payingUnit = getDomainObject(request, "unitOID");
	List<Unit> units = new ArrayList<Unit>();
	units.add(payingUnit);
	try {
	    genericActivityExecution(request, "GenericRemovePayingUnit", units);
	} catch (DomainException e) {
	    addErrorMessage(e.getMessage(), getBundle());
	}
	return executeGenericRemovePayingUnit(mapping, form, request, response);
    }

    public ActionForward executeGenericAssignPayingUnitToItem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final RequestItem item = getRequestItem(request);
	final PaymentProcess process = getProcess(request);
	List<UnitItemBean> beans = new ArrayList<UnitItemBean>();
	for (Unit unit : process.getPayingUnits()) {
	    beans.add(new UnitItemBean(unit, item));
	}
	request.setAttribute("item", item);
	request.setAttribute("process", process);
	request.setAttribute("unitItemBeans", beans);

	return mapping.findForward("assign.unit.item");
    }

    public ActionForward executeAssignPayingUnitToItemCreation(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final PaymentProcess process = getProcess(request);
	final RequestItem item = getRequestItem(request);

	List<UnitItemBean> beans = getRenderedObject("unitItemBeans");
	try {
	    genericActivityExecution(process, "GenericAssignPayingUnitToItem", item, beans);
	} catch (DomainException e) {
	    addErrorMessage(e.getMessage(), getBundle());
	    return executeGenericAssignPayingUnitToItem(mapping, form, request, response);
	}

	return viewProcess(mapping, form, request, response);
    }

    public ActionForward executeDistributeRealValuesForPayingUnits(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final RequestItem item = getRequestItem(request);
	List<UnitItemBean> beans = new ArrayList<UnitItemBean>();

	for (UnitItem unitItem : item.getUnitItems()) {
	    beans.add(new UnitItemBean(unitItem));
	}
	request.setAttribute("item", item);
	request.setAttribute("unitItemBeans", beans);

	return mapping.findForward("edit.real.shares.values");
    }

    public ActionForward executeDistributeRealValuesForPayingUnitsEdition(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final RequestItem item = getRequestItem(request);
	List<UnitItemBean> beans = getRenderedObject("unitItemBeans");

	try {
	    return executeActivityAndViewProcess(mapping, form, request, response, "DistributeRealValuesForPayingUnits", beans,
		    item);
	} catch (DomainException e) {
	    addErrorMessage(e.getMessage(), getBundle());
	    request.setAttribute("item", item);
	    request.setAttribute("unitItemBeans", beans);
	    return mapping.findForward("edit.real.shares.values");
	}
    }

    public ActionForward calculateShareValuePostBack(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return genericCalculateShareValuePostBack(mapping, form, request, response, "assign.unit.item", false);
    }

    public ActionForward calculateRealShareValuePostBack(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final RequestItem item = getRequestItem(request);
	request.setAttribute("item", item);
	return genericCalculateShareValuePostBack(mapping, form, request, response, "edit.real.shares.values", true);
    }

    private ActionForward genericCalculateShareValuePostBack(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response, String forward, boolean realValue) {

	final PaymentProcess process = getProcess(request);
	final RequestItem item = getRequestItem(request);

	List<UnitItemBean> beans = getRenderedObject("unitItemBeans");
	int assigned = 0;
	for (UnitItemBean bean : beans) {
	    if (bean.getAssigned()) {
		assigned++;
	    }
	}
	if (assigned != 0) {
	    Money[] shareValues;
	    Money totalItemValue = realValue ? item.getRealValue() : item.getValue();
	    shareValues = totalItemValue.allocate(assigned);

	    int i = 0;
	    for (UnitItemBean bean : beans) {
		if (bean.getAssigned()) {
		    if (realValue) {
			bean.setRealShareValue(shareValues[i++]);
		    } else {
			bean.setShareValue(shareValues[i++]);
		    }
		} else {
		    if (realValue) {
			bean.setRealShareValue(null);
		    } else {
			bean.setShareValue(null);
		    }
		}
	    }
	}
	request.setAttribute("item", item);
	request.setAttribute("process", process);
	request.setAttribute("unitItemBeans", beans);

	RenderUtils.invalidateViewState();
	return mapping.findForward(forward);
    }

    public ActionForward executeProjectFundAllocation(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final PaymentProcess process = getProcess(request);
	final User user = UserView.getUser();
	if (process.getCurrentOwner() == null || (user != null && process.getCurrentOwner() == user.getPerson())) {
	    if (process.getCurrentOwner() == null) {
		process.takeProcess();
	    }
	    request.setAttribute("process", process);
	    List<FundAllocationBean> fundAllocationBeans = new ArrayList<FundAllocationBean>();
	    for (Financer financer : process.getProjectFinancersWithFundsAllocated(user.getPerson())) {
		fundAllocationBeans.add(new FundAllocationBean(financer));
	    }
	    request.setAttribute("fundAllocationBeans", fundAllocationBeans);
	    return mapping.findForward("allocate.project.funds");
	} else {
	    return viewProcess(mapping, form, request, response);
	}
    }

    public ActionForward allocateProjectFunds(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final PaymentProcess process = getProcess(request);
	final List<FundAllocationBean> fundAllocationBeans = getRenderedObject();
	genericActivityExecution(process, "ProjectFundAllocation", fundAllocationBeans);
	return viewProcess(mapping, form, request, response);
    }

    public ActionForward executeFundAllocation(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final PaymentProcess process = getProcess(request);
	final User user = UserView.getUser();
	if (process.getCurrentOwner() == null || (user != null && process.getCurrentOwner() == user.getPerson())) {
	    if (process.getCurrentOwner() == null) {
		process.takeProcess();
	    }
	    request.setAttribute("process", process);
	    List<FundAllocationBean> fundAllocationBeans = new ArrayList<FundAllocationBean>();
	    for (Financer financer : process.getFinancersWithFundsAllocated(user.getPerson())) {
		fundAllocationBeans.add(new FundAllocationBean(financer));
	    }
	    request.setAttribute("fundAllocationBeans", fundAllocationBeans);
	    return mapping.findForward("allocate.funds");
	} else {
	    return viewProcess(mapping, form, request, response);
	}
    }

    public ActionForward allocateFunds(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final PaymentProcess process = getProcess(request);
	final List<FundAllocationBean> fundAllocationBeans = getRenderedObject();
	genericActivityExecution(process, "FundAllocation", fundAllocationBeans);
	return viewProcess(mapping, form, request, response);
    }

    public ActionForward executeRemoveFundAllocation(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "RemoveFundAllocation");
    }

    public ActionForward executeRemoveProjectFundAllocation(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "RemoveProjectFundAllocation");
    }

    public ActionForward executeAllocateFundsPermanently(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final PaymentProcess process = getProcess(request);
	final User user = UserView.getUser();
	if (process.getCurrentOwner() == null || (user != null && process.getCurrentOwner() == user.getPerson())) {
	    if (process.getCurrentOwner() == null) {
		process.takeProcess();
	    }
	    request.setAttribute("process", process);
	    List<FundAllocationBean> fundAllocationBeans = new ArrayList<FundAllocationBean>();
	    for (Financer financer : process.getFinancersWithFundsAllocated()) {
		FundAllocationBean fundAllocationBean = new FundAllocationBean(financer);
		fundAllocationBean.setFundAllocationId(financer.getFundAllocationId());
		fundAllocationBean.setEffectiveFundAllocationId(financer.getFundAllocationId());
		fundAllocationBeans.add(fundAllocationBean);
	    }
	    request.setAttribute("fundAllocationBeans", fundAllocationBeans);

	    return mapping.findForward("allocate.effective.funds");
	} else {
	    return viewProcess(mapping, form, request, response);
	}
    }

    public ActionForward allocateFundsPermanently(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final PaymentProcess process = getProcess(request);
	final List<FundAllocationBean> fundAllocationBeans = getRenderedObject();
	try {
	    genericActivityExecution(process, "AllocateFundsPermanently", fundAllocationBeans);
	} catch (DomainException e) {
	    request.setAttribute("fundAllocationBeans", fundAllocationBeans);
	    request.setAttribute("process", process);
	    addMessage(e.getMessage(), getBundle());
	    return mapping.findForward("allocate.effective.funds");
	}
	return viewProcess(mapping, form, request, response);
    }

    public ActionForward executeRemoveFundsPermanentlyAllocated(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "RemoveFundsPermanentlyAllocated");
    }

    public ActionForward executeAllocateProjectFundsPermanently(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final PaymentProcess process = getProcess(request);
	final User user = UserView.getUser();
	if (process.getCurrentOwner() == null || (user != null && process.getCurrentOwner() == user.getPerson())) {
	    if (process.getCurrentOwner() == null) {
		process.takeProcess();
	    }
	    request.setAttribute("process", process);
	    List<FundAllocationBean> fundAllocationBeans = new ArrayList<FundAllocationBean>();
	    for (Financer financer : process.getFinancersWithFundsAllocated()) {
		if (financer.isProjectFinancer()) {
		    final ProjectFinancer projectFinancer = (ProjectFinancer) financer;
		    FundAllocationBean fundAllocationBean = new FundAllocationBean(projectFinancer);
		    fundAllocationBean.setFundAllocationId(projectFinancer.getProjectFundAllocationId());
		    fundAllocationBean.setEffectiveFundAllocationId(projectFinancer.getProjectFundAllocationId());
		    fundAllocationBeans.add(fundAllocationBean);
		}
	    }
	    request.setAttribute("fundAllocationBeans", fundAllocationBeans);
	    return mapping.findForward("allocate.effective.project.funds");
	} else {
	    return viewProcess(mapping, form, request, response);
	}
    }

    public ActionForward allocateProjectFundsPermanently(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final PaymentProcess process = getProcess(request);
	final List<FundAllocationBean> fundAllocationBeans = getRenderedObject();
	genericActivityExecution(process, "AllocateProjectFundsPermanently", fundAllocationBeans);
	return viewProcess(mapping, form, request, response);
    }

    public ActionForward addAllocationFundForProject(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return addAllocationFundGeneric(mapping, request, "financerFundAllocationId", "allocate.effective.project.funds");
    }

    public ActionForward removeAllocationFundForProject(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return removeAllocationFundGeneric(mapping, request, "financerFundAllocationId", "allocate.effective.project.funds");
    }

    private ActionForward addAllocationFundGeneric(final ActionMapping mapping, final HttpServletRequest request,
	    String viewStateID, String forward) {

	final PaymentProcess process = getProcess(request);
	request.setAttribute("process", process);
	List<FundAllocationBean> fundAllocationBeans = getRenderedObject(viewStateID);
	Integer index = Integer.valueOf(request.getParameter("index"));

	Financer financer = getDomainObject(request, "financerOID");
	FundAllocationBean fundAllocationBean = new FundAllocationBean(financer);
	fundAllocationBean.setFundAllocationId(null);
	fundAllocationBean.setEffectiveFundAllocationId(null);
	fundAllocationBean.setAllowedToAddNewFund(false);

	fundAllocationBeans.add(index + 1, fundAllocationBean);
	request.setAttribute("fundAllocationBeans", fundAllocationBeans);
	RenderUtils.invalidateViewState();
	return mapping.findForward(forward);
    }

    private ActionForward removeAllocationFundGeneric(final ActionMapping mapping, final HttpServletRequest request,
	    String viewStateID, String forward) {
	final PaymentProcess process = getProcess(request);
	request.setAttribute("process", process);
	List<FundAllocationBean> fundAllocationBeans = getRenderedObject(viewStateID);
	int index = Integer.valueOf(request.getParameter("index")).intValue();

	fundAllocationBeans.remove(index);
	request.setAttribute("fundAllocationBeans", fundAllocationBeans);
	RenderUtils.invalidateViewState();
	return mapping.findForward(forward);
    }

    public ActionForward addAllocationFund(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	return addAllocationFundGeneric(mapping, request, "financerFundAllocationId", "allocate.effective.funds");
    }

    public ActionForward removeAllocationFund(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return removeAllocationFundGeneric(mapping, request, "financerFundAllocationId", "allocate.effective.funds");
    }

    @Override
    protected String getBundle() {
	return "ACQUISITION_RESOURCES";
    }

    public ActionForward executeActivityAndViewProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response, final String activityName, Object... args) {
	genericActivityExecution(request, activityName, args);
	return viewProcess(mapping, form, request, response);
    }

    protected <T extends RequestItem> T getRequestItem(HttpServletRequest request) {
	return (T) getDomainObject(request, "itemOid");
    }
}
