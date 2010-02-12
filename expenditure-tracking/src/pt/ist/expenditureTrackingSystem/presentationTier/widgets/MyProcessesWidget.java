package pt.ist.expenditureTrackingSystem.presentationTier.widgets;

import java.util.Collections;
import java.util.List;

import pt.ist.fenixWebFramework.services.Service;

import module.dashBoard.domain.DashBoardWidget;
import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.WidgetController;
import myorg.util.ClassNameBundle;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureWidgetOptions;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

@ClassNameBundle(bundle = "resources/ExpenditureResources", key = "process.title.myProcesses")
public class MyProcessesWidget extends WidgetController {

    public static Predicate NOT_PAYED_PROCESS_PREDICATE = new Predicate() {
	@Override
	public boolean evaluate(Object arg0) {
	    if (arg0 instanceof AcquisitionProcess) {
		return !((AcquisitionProcess) arg0).isPayed();
	    }
	    return true;
	}
    };

    @Override
    public boolean isOptionsModeSupported() {
	return true;
    }

    @Override
    @Service
    protected ExpenditureWidgetOptions getOrCreateOptions(DashBoardWidget widget) {
	ExpenditureWidgetOptions options = (ExpenditureWidgetOptions) widget.getOptions();
	if (options == null) {
	    options = new ExpenditureWidgetOptions(10);
	    widget.setOptions(options);
	}
	return options;
    }

    @Override
    public void doView(WidgetRequest request) {
	DashBoardWidget widget = request.getWidget();
	ExpenditureWidgetOptions options = getOrCreateOptions(widget);
	Person loggedPerson = Person.getLoggedPerson();

	List<PaymentProcess> myProcesses = loggedPerson.getAcquisitionProcesses(PaymentProcess.class);
	CollectionUtils.filter(myProcesses, NOT_PAYED_PROCESS_PREDICATE);
	Collections.sort(myProcesses, new ReverseComparator(new BeanComparator("acquisitionProcessId")));
	myProcesses = myProcesses.subList(0, Math.min(options.getMaxListSize(), myProcesses.size()));

	request.setAttribute("widgetOptions-" + widget.getExternalId(), options);
	request.setAttribute("ownProcesses", myProcesses);
	request.setAttribute("person", loggedPerson);
    }

    @Override
    public void doEditOptions(WidgetRequest request) {
	DashBoardWidget widget = request.getWidget();
	request.setAttribute("edit-widgetOptions-" + widget.getExternalId(), getOrCreateOptions(widget));
    }
}
