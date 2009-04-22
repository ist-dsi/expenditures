package pt.ist.expenditureTrackingSystem.presentationTier.actions.dashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myorg.presentationTier.actions.ContextBaseAction;
import myorg.util.Counter;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchPaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter.ChecksumPredicate;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.Strings;

@Mapping(path = "/dashBoard")
public class DashBoardAction extends ContextBaseAction {

    static {
	RequestChecksumFilter.registerFilterRule(new ChecksumPredicate() {
	    public boolean shouldFilter(HttpServletRequest httpServletRequest) {
		return !(httpServletRequest.getRequestURI().endsWith("/dashBoard.do")
			&& httpServletRequest.getQueryString() != null && httpServletRequest.getQueryString().contains(
			"method=order"));
	    }
	});
    }

    public ActionForward order(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws Exception {

	Person person = Person.getLoggedPerson();

	String column1 = request.getParameter("column1");
	String column2 = request.getParameter("column2");
	String column3 = request.getParameter("column3");

	person.getDashBoard().edit(getStrings(column1), getStrings(column2), getStrings(column3));
	return null;
    }

    public ActionForward viewDigest(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	Person loggedPerson = Person.getLoggedPerson();
	Map<AcquisitionProcessStateType, Counter<AcquisitionProcessStateType>> simplifiedMap = loggedPerson
		.generateAcquisitionMap();
	List<Counter<AcquisitionProcessStateType>> counters = new ArrayList<Counter<AcquisitionProcessStateType>>();
	counters.addAll(simplifiedMap.values());
	Collections.sort(counters, new BeanComparator("countableObject"));
	request.setAttribute("simplifiedCounters", counters);

	Map<RefundProcessStateType, Counter<RefundProcessStateType>> refundMap = loggedPerson.generateRefundMap();
	List<Counter<RefundProcessStateType>> refundCounters = new ArrayList<Counter<RefundProcessStateType>>();
	refundCounters.addAll(refundMap.values());
	Collections.sort(refundCounters, new BeanComparator("countableObject"));
	request.setAttribute("refundCounters", refundCounters);

	List<PaymentProcess> myProcesses = loggedPerson.getAcquisitionProcesses(PaymentProcess.class);
	Collections.sort(myProcesses, new ReverseComparator(new BeanComparator("acquisitionProcessId")));
	request.setAttribute("ownProcesses", myProcesses.subList(0, Math.min(10, myProcesses.size())));

	List<PaymentProcess> takenProcesses = loggedPerson.getProcesses(PaymentProcess.class);
	request.setAttribute("takenProcesses", takenProcesses.subList(0, Math.min(10, takenProcesses.size())));

	request.setAttribute("searchBean", new SearchPaymentProcess());

	List<PaymentProcess> processesWhereUserWasInvolvedWithUnreadComments = new ArrayList<PaymentProcess>();
	processesWhereUserWasInvolvedWithUnreadComments.addAll(loggedPerson
		.getProcessesWhereUserWasInvolvedWithUnreadComments(PaymentProcess.class));
	Collections.sort(processesWhereUserWasInvolvedWithUnreadComments, new ReverseComparator(new BeanComparator(
		"acquisitionProcessId")));

	request.setAttribute("processesWithUnreadComments", processesWhereUserWasInvolvedWithUnreadComments);

	return forward(request, "/acquisitions/search/digest.jsp");
    }

    public ActionForward quickAccess(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	SearchPaymentProcess searchBean = getRenderedObject("quickAccess");
	searchBean.setHasAvailableAndAccessibleActivityForUser(Boolean.FALSE);
	Set<PaymentProcess> search = searchBean.search();

	if (search.size() != 1) {
	    request.setAttribute("widgetQuickView.messages", "widget.widgetQuickView.noProcessFound");
	    return viewDigest(mapping, form, request, response);
	}

	PaymentProcess process = search.iterator().next();

	return new ActionForward("/acquisition" + process.getClass().getSimpleName() + ".do?method=viewProcess&processOid="
		+ process.getOID());
    }

    public ActionForward markAllCommentsAsRead(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	Person loggedPerson = Person.getLoggedPerson();
	for (PaymentProcess process : loggedPerson.getProcessesWhereUserWasInvolvedWithUnreadComments(PaymentProcess.class)) {
	    process.markCommentsAsReadForPerson(loggedPerson);
	}

	return viewDigest(mapping, form, request, response);
    }

    private Strings getStrings(String column1) {
	String[] split = column1.substring(0, column1.length()).split(",");
	List<String> stringList = new ArrayList<String>();
	for (String string : split) {
	    if (string.length() > 0) {
		stringList.add(string);
	    }
	}
	Strings strings = new Strings(stringList);
	return strings;
    }

}
