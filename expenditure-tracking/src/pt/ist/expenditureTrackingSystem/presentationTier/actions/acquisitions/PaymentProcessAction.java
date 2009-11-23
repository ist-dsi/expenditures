package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myorg.domain.util.Money;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;
import pt.ist.expenditureTrackingSystem.domain.dto.DomainObjectBean;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;
import pt.ist.expenditureTrackingSystem.domain.dto.UnitItemBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.ProcessAction;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.Strings;

public abstract class PaymentProcessAction extends ProcessAction {

    protected abstract String getSelectUnitToAddForwardUrl();

    public ActionForward executeGenericAddPayingUnit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final PaymentProcess process = getProcess(request);
	request.setAttribute("process", process);
	request.setAttribute("domainObjectBean", new DomainObjectBean<Unit>());
	return forward(request, getSelectUnitToAddForwardUrl());
    }

    public ActionForward addPayingUnit(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	DomainObjectBean<Unit> bean = getRenderedObject("unitToAdd");
	List<Unit> units = new ArrayList<Unit>();
	units.add(bean.getDomainObject());
	genericActivityExecution(request, "GenericAddPayingUnit", units);
	return viewProcess(mapping, form, request, response);
    }

    protected abstract String getRemovePayingUnitsForwardUrl();

    public ActionForward executeGenericRemovePayingUnit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final PaymentProcess process = getProcess(request);
	request.setAttribute("process", process);
	request.setAttribute("payingUnits", process.getPayingUnits());
	return forward(request, getRemovePayingUnitsForwardUrl());
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

    protected abstract String getAssignUnitItemForwardUrl();

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

	return forward(request, getAssignUnitItemForwardUrl());
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

    protected abstract String getEditRealShareValuesForwardUrl();

    public ActionForward executeDistributeRealValuesForPayingUnits(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final RequestItem item = getRequestItem(request);
	List<UnitItemBean> beans = new ArrayList<UnitItemBean>();

	for (UnitItem unitItem : item.getUnitItems()) {
	    beans.add(new UnitItemBean(unitItem));
	}
	request.setAttribute("item", item);
	request.setAttribute("unitItemBeans", beans);

	return forward(request, getEditRealShareValuesForwardUrl());
    }

    public ActionForward executeDistributeRealValuesForPayingUnitsEdition(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final RequestItem item = getRequestItem(request);
	List<UnitItemBean> beans = getRenderedObject("unitItemBeans");
	PaymentProcess process = getProcess(request);
	try {
	    genericActivityExecution(process, "DistributeRealValuesForPayingUnits", beans, item);
	    return viewProcess(mapping, form, request, response);
	} catch (DomainException e) {
	    addErrorMessage(e.getMessage(), getBundle());
	    request.setAttribute("item", item);
	    request.setAttribute("unitItemBeans", beans);
	    return forward(request, getEditRealShareValuesForwardUrl());
	}
    }

    public ActionForward calculateShareValuePostBack(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return genericCalculateShareValuePostBack(mapping, form, request, response, getAssignUnitItemForwardUrl(), false);
    }

    public ActionForward calculateRealShareValuePostBack(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final RequestItem item = getRequestItem(request);
	request.setAttribute("item", item);
	return genericCalculateShareValuePostBack(mapping, form, request, response, getEditRealShareValuesForwardUrl(), true);
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
	return forward(request, forward);
    }

    public ActionForward executeProjectFundAllocation(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final PaymentProcess process = getProcess(request);
	final Person person = getLoggedPerson();
	if (process.getCurrentOwner() == null || (person != null && process.isTakenByPerson(person.getUser()))) {
	    if (process.getCurrentOwner() == null) {
		process.takeProcess();
	    }
	    request.setAttribute("process", process);
	    List<FundAllocationBean> fundAllocationBeans = new ArrayList<FundAllocationBean>();
	    for (Financer financer : process.getProjectFinancersWithFundsAllocated(person)) {
		fundAllocationBeans.add(new FundAllocationBean(financer));
	    }
	    request.setAttribute("fundAllocationBeans", fundAllocationBeans);
	    return forward(request, "/acquisitions/commons/allocateProjectFunds.jsp");
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
	final Person person = getLoggedPerson();
	if (process.getCurrentOwner() == null || (person != null && process.isTakenByPerson(person.getUser()))) {
	    if (process.getCurrentOwner() == null) {
		process.takeProcess();
	    }
	    request.setAttribute("process", process);
	    List<FundAllocationBean> fundAllocationBeans = new ArrayList<FundAllocationBean>();
	    for (Financer financer : process.getFinancersWithFundsAllocated(person)) {
		fundAllocationBeans.add(new FundAllocationBean(financer));
	    }
	    request.setAttribute("fundAllocationBeans", fundAllocationBeans);
	    return forward(request, "/acquisitions/commons/allocateFunds.jsp");
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
	final Person person = getLoggedPerson();
	if (process.getCurrentOwner() == null || (person != null && process.isTakenByPerson(person.getUser()))) {
	    if (process.getCurrentOwner() == null) {
		process.takeProcess();
	    }
	    request.setAttribute("process", process);
	    List<FundAllocationBean> fundAllocationBeans = new ArrayList<FundAllocationBean>();
	    for (Financer financer : process.getFinancersWithFundsAllocated()) {
		fundAllocationBeans.addAll(getFundAllocationBeans(financer));
	    }
	    request.setAttribute("fundAllocationBeans", fundAllocationBeans);

	    return forward(request, "/acquisitions/commons/allocateEffectiveFunds.jsp");
	} else {
	    return viewProcess(mapping, form, request, response);
	}
    }

    private List<FundAllocationBean> getFundAllocationBeans(Financer financer) {
	List<FundAllocationBean> beans = new ArrayList<FundAllocationBean>();
	Strings effectiveFunds = financer.getEffectiveFundAllocationId();

	if (effectiveFunds == null) {
	    FundAllocationBean fundAllocationBean = new FundAllocationBean(financer);
	    fundAllocationBean.setFundAllocationId(financer.getFundAllocationId());
	    fundAllocationBean.setEffectiveFundAllocationId(financer.getFundAllocationId());
	    fundAllocationBean.setAllowedToAddNewFund(true);
	    beans.add(fundAllocationBean);
	} else {
	    int i = 0;
	    for (String effectiveFund : effectiveFunds) {
		FundAllocationBean fundAllocationBean = new FundAllocationBean(financer);
		fundAllocationBean.setFundAllocationId(financer.getFundAllocationId());
		fundAllocationBean.setEffectiveFundAllocationId(effectiveFund);
		fundAllocationBean.setAllowedToAddNewFund(i++ == 0);
		beans.add(fundAllocationBean);
	    }
	}

	return beans;
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
	    return forward(request, "/acquisitions/commons/allocateEffectiveFunds.jsp");
	}
	return viewProcess(mapping, form, request, response);
    }

    public ActionForward executeRemoveFundsPermanentlyAllocated(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "RemoveFundsPermanentlyAllocated");
    }

    public ActionForward executeRemovePermanentProjectFunds(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "RemovePermanentProjectFunds");
    }

    public ActionForward executeAllocateProjectFundsPermanently(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final PaymentProcess process = getProcess(request);
	final Person person = getLoggedPerson();
	if (process.getCurrentOwner() == null || (person != null && process.isTakenByPerson(person.getUser()))) {
	    if (process.getCurrentOwner() == null) {
		process.takeProcess();
	    }
	    request.setAttribute("process", process);
	    List<FundAllocationBean> fundAllocationBeans = new ArrayList<FundAllocationBean>();
	    for (Financer financer : process.getFinancersWithFundsAllocated()) {
		if (financer.isProjectFinancer()) {
		    fundAllocationBeans.addAll(getProjectFundAllocationBeans((ProjectFinancer) financer));
		}
	    }
	    request.setAttribute("fundAllocationBeans", fundAllocationBeans);
	    return forward(request, "/acquisitions/commons/allocateEffectiveProjectFunds.jsp");
	} else {
	    return viewProcess(mapping, form, request, response);
	}
    }

    private List<FundAllocationBean> getProjectFundAllocationBeans(ProjectFinancer projectFinancer) {
	List<FundAllocationBean> beans = new ArrayList<FundAllocationBean>();
	Strings effectiveFunds = projectFinancer.getEffectiveProjectFundAllocationId();

	if (effectiveFunds == null) {
	    FundAllocationBean fundAllocationBean = new FundAllocationBean(projectFinancer);
	    fundAllocationBean.setFundAllocationId(projectFinancer.getProjectFundAllocationId());
	    fundAllocationBean.setEffectiveFundAllocationId(projectFinancer.getProjectFundAllocationId());
	    fundAllocationBean.setAllowedToAddNewFund(true);
	    beans.add(fundAllocationBean);
	} else {
	    int i = 0;
	    for (String effectiveFund : effectiveFunds) {
		FundAllocationBean fundAllocationBean = new FundAllocationBean(projectFinancer);
		fundAllocationBean.setFundAllocationId(projectFinancer.getProjectFundAllocationId());
		fundAllocationBean.setEffectiveFundAllocationId(effectiveFund);
		fundAllocationBean.setAllowedToAddNewFund(i++ == 0);
		beans.add(fundAllocationBean);
	    }
	}

	return beans;
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

	return addAllocationFundGeneric(mapping, request, "financerFundAllocationId",
		"/acquisitions/commons/allocateEffectiveProjectFunds.jsp");
    }

    public ActionForward removeAllocationFundForProject(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return removeAllocationFundGeneric(mapping, request, "financerFundAllocationId",
		"/acquisitions/commons/allocateEffectiveProjectFunds.jsp");
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
	return forward(request, forward);
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
	return forward(request, forward);
    }

    public ActionForward addAllocationFund(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	return addAllocationFundGeneric(mapping, request, "financerFundAllocationId",
		"/acquisitions/commons/allocateEffectiveFunds.jsp");
    }

    public ActionForward removeAllocationFund(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return removeAllocationFundGeneric(mapping, request, "financerFundAllocationId",
		"/acquisitions/commons/allocateEffectiveFunds.jsp");
    }

    public ActionForward executeUnAuthorize(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "UnAuthorize");
    }

    public ActionForward executeAuthorize(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	return executeActivityAndViewProcess(mapping, form, request, response, "Authorize");
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
