package pt.ist.expenditureTrackingSystem.presentationTier.widgets;

import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.WidgetController;
import myorg.util.BundleUtil;
import myorg.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

@ClassNameBundle(bundle = "resources/ExpenditureResources", key = "process.title.emailNotification")
public class ActivateEmailNotificationWidget extends WidgetController {

    @Override
    public void doView(WidgetRequest request) {
	request.setAttribute("person", Person.getLoggedPerson());
    }

    @Override
    public String getWidgetDescription() {
	return BundleUtil.getStringFromResourceBundle("resources/ExpenditureResources",
		"widget.description.ActivateEmailNotificationWidget");
    }
}
