package pt.ist.expenditureTrackingSystem.presentationTier.actions.dashboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.workflow.presentationTier.actions.ProcessManagement;
import module.workflow.util.ProcessEvaluator;
import myorg.presentationTier.actions.ContextBaseAction;
import myorg.util.Counter;
import myorg.util.MultiCounter;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchPaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.expenditureTrackingSystem.util.AcquisitionProcessStateTypeCounter;
import pt.ist.expenditureTrackingSystem.util.ProcessMapGenerator;
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

//	long start1 = System.currentTimeMillis();
//	final AcquisitionProcessStateTypeCounter simplifiedProcedureProcessStateTypeCounter = new AcquisitionProcessStateTypeCounter();
//	final AcquisitionProcessStateTypeCounter prioritySimplifiedProcedureProcessStateTypeCounter = new AcquisitionProcessStateTypeCounter();
//
//	final ProcessEvaluator simplifiedProcedureProcessEvaluator = new ProcessEvaluator<SimplifiedProcedureProcess>() {
//
//	    @Override
//	    public void evaluate(final SimplifiedProcedureProcess process) {
//		final AcquisitionProcessStateType type = process.getAcquisitionProcessStateType();
//		simplifiedProcedureProcessStateTypeCounter.count(type);
//		if (process.isPriorityProcess()) {
//		    prioritySimplifiedProcedureProcessStateTypeCounter.count(type);
//		}
//		super.evaluate(process);
//	    }
//	    
//	};
//	GenericProcess.evaluateProcessesForPerson((Class) SimplifiedProcedureProcess.class, loggedPerson, null, true,
//		simplifiedProcedureProcessEvaluator);
//
//	long end1 = System.currentTimeMillis();
//	System.out.println("Testing: " + (end1 - start1) + "ms.");
//	request.setAttribute("simplifiedProcedureProcessStateTypeCounter", simplifiedProcedureProcessStateTypeCounter);
//	request.setAttribute("prioritySimplifiedProcedureProcessStateTypeCounter", prioritySimplifiedProcedureProcessStateTypeCounter);

	long start2 = System.currentTimeMillis();
	Map<AcquisitionProcessStateType, MultiCounter<AcquisitionProcessStateType>> simplifiedMap = ProcessMapGenerator
		.generateAcquisitionMap(loggedPerson);
	List<Counter<AcquisitionProcessStateType>> counters = new ArrayList<Counter<AcquisitionProcessStateType>>();
	List<Counter<AcquisitionProcessStateType>> priorityCounters = new ArrayList<Counter<AcquisitionProcessStateType>>();

	for (MultiCounter<AcquisitionProcessStateType> multiCounter : simplifiedMap.values()) {
	    Counter<AcquisitionProcessStateType> defaultCounter = ProcessMapGenerator.getDefaultCounter(multiCounter);
	    Counter<AcquisitionProcessStateType> priorityCounter = ProcessMapGenerator.getPriorityCounter(multiCounter);

	    if (defaultCounter != null) {
		counters.add(defaultCounter);
	    }
	    if (priorityCounter != null) {
		priorityCounters.add(priorityCounter);
	    }
	}

	Collections.sort(counters, new BeanComparator("countableObject"));
	Collections.sort(priorityCounters, new BeanComparator("countableObject"));

	long end2 = System.currentTimeMillis();
	System.out.println("Current: " + (end2 - start2) + "ms.");
	request.setAttribute("simplifiedCounters", counters);
	request.setAttribute("simplifiedCounters-priority", priorityCounters);

	Map<RefundProcessStateType, MultiCounter<RefundProcessStateType>> refundMap = ProcessMapGenerator
		.generateRefundMap(loggedPerson);
	List<Counter<RefundProcessStateType>> refundCounters = new ArrayList<Counter<RefundProcessStateType>>();
	List<Counter<RefundProcessStateType>> priorityRefundCounters = new ArrayList<Counter<RefundProcessStateType>>();

	for (MultiCounter<RefundProcessStateType> multiCounter : refundMap.values()) {
	    Counter<RefundProcessStateType> defaultCounter = ProcessMapGenerator.getDefaultCounter(multiCounter);
	    Counter<RefundProcessStateType> priorityCounter = ProcessMapGenerator.getPriorityCounter(multiCounter);

	    if (defaultCounter != null) {
		refundCounters.add(defaultCounter);
	    }
	    if (priorityCounter != null) {
		priorityRefundCounters.add(priorityCounter);
	    }
	}

	Collections.sort(refundCounters, new BeanComparator("countableObject"));
	Collections.sort(priorityRefundCounters, new BeanComparator("countableObject"));
	request.setAttribute("refundCounters", refundCounters);
	request.setAttribute("refundCounters-priority", priorityRefundCounters);

	List<PaymentProcess> myProcesses = loggedPerson.getAcquisitionProcesses(PaymentProcess.class);
	CollectionUtils.filter(myProcesses, new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		if (arg0 instanceof AcquisitionProcess) {
		    return !((AcquisitionProcess) arg0).isPayed();
		}

		return true;
	    }

	});
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

	int unreadProcessCount = processesWhereUserWasInvolvedWithUnreadComments.size();
	request.setAttribute("processesWithUnreadComments", processesWhereUserWasInvolvedWithUnreadComments.subList(0, Math.min(
		10, unreadProcessCount)));

	request.setAttribute("unreadProcessesCount", unreadProcessCount);
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

	return ProcessManagement.forwardToProcess(process);
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
