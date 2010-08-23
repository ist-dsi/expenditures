package pt.ist.expenditureTrackingSystem.presentationTier.widgets;

import java.util.List;
import pt.ist.fenixWebFramework.services.Service;

import module.dashBoard.domain.DashBoardWidget;
import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.WidgetController;
import myorg.util.BundleUtil;
import myorg.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureWidgetOptions;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

@ClassNameBundle(bundle = "resources/ExpenditureResources", key = "process.title.takenProcesses")
public class TakenProcessesWidget extends WidgetController {

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
    public boolean isOptionsModeSupported() {
	return true;
    }

    @Override
    public void doView(WidgetRequest request) {
	DashBoardWidget widget = request.getWidget();
	ExpenditureWidgetOptions options = getOrCreateOptions(widget);
	Person loggedPerson = Person.getLoggedPerson();

	List<PaymentProcess> takenProcesses = loggedPerson.getProcesses(PaymentProcess.class);
	takenProcesses = takenProcesses.subList(0, Math.min(options.getMaxListSize(), takenProcesses.size()));

	request.setAttribute("widgetOptions-" + widget.getExternalId(), options);
	request.setAttribute("takenProcesses", takenProcesses);
	request.setAttribute("person", loggedPerson);
    }

    @Override
    public void doEditOptions(WidgetRequest request) {
	DashBoardWidget widget = request.getWidget();
	request.setAttribute("edit-widgetOptions-" + widget.getExternalId(), getOrCreateOptions(widget));
    }
    
    @Override
    public String getWidgetDescription() {
	return BundleUtil.getStringFromResourceBundle("resources/ExpenditureResources", "widget.description.TakenProcessesWidget");
    }
}
