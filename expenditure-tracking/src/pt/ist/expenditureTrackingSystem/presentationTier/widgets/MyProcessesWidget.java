package pt.ist.expenditureTrackingSystem.presentationTier.widgets;

import java.util.Collections;
import java.util.List;

import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.WidgetController;
import myorg.util.ClassNameBundle;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

@ClassNameBundle(bundle = "resources/ExpenditureResources", key = "process.title.myProcesses")
public class MyProcessesWidget extends WidgetController {

    @Override
    public void doView(WidgetRequest request) {
	Person loggedPerson = Person.getLoggedPerson();
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
	request.setAttribute("person", loggedPerson);
    }
}
