package pt.ist.expenditureTrackingSystem.presentationTier.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.WidgetController;
import myorg.util.ClassNameBundle;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

@ClassNameBundle(bundle = "resources/ExpenditureResources", key = "process.title.processesWithUnreadComments")
public class UnreadCommentsWidget extends WidgetController {

    @Override
    public void doView(WidgetRequest request) {
	List<PaymentProcess> processesWithUnreadComments = new ArrayList<PaymentProcess>();
	processesWithUnreadComments.addAll(Person.getLoggedPerson().getProcessesWhereUserWasInvolvedWithUnreadComments(
		PaymentProcess.class));
	Collections.sort(processesWithUnreadComments, new ReverseComparator(new BeanComparator("acquisitionProcessId")));

	int unreadProcessCount = processesWithUnreadComments.size();
	request.setAttribute("processesWithUnreadComments", processesWithUnreadComments.subList(0, Math.min(10,
		unreadProcessCount)));
    }
}
