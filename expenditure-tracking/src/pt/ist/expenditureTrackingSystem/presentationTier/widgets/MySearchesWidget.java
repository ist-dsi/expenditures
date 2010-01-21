package pt.ist.expenditureTrackingSystem.presentationTier.widgets;

import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.WidgetController;
import myorg.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

@ClassNameBundle(bundle = "resources/ExpenditureResources", key = "process.title.mySearchs")
public class MySearchesWidget extends WidgetController {

    @Override
    public void doView(WidgetRequest request) {
	request.setAttribute("person", Person.getLoggedPerson());
    }
}
