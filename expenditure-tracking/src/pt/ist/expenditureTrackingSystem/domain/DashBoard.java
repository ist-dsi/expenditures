package pt.ist.expenditureTrackingSystem.domain;

import java.util.ArrayList;
import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.Strings;

public class DashBoard extends DashBoard_Base {

    public DashBoard(Person person) {
	super();
	setPerson(person);
	reset();
    }

    @Service
    public void reset() {
	List<String> column1 = new ArrayList<String>();
	List<String> column2 = new ArrayList<String>();
	List<String> column3 = new ArrayList<String>();
	column1.add("widgetMyProcesses");
	column1.add("widgetMySearches");
	column1.add("widgetUnreadComments");
	column2.add("widgetPendingSimplified");
	column2.add("widgetPendingRefund");
	column3.add("widgetTakenProcesses");
	column3.add("widgetQuickView");
	column3.add("widgetActivateEmailNotification");
	setColumn1(new Strings(column1));
	setColumn2(new Strings(column2));
	setColumn3(new Strings(column3));
    }

    @Service
    public void edit(Strings column1, Strings column2, Strings column3) {
	setColumn1(column1);
	setColumn2(column2);
	setColumn3(column3);
    }

    @Service
    public static DashBoard newDashBoard(Person person) {
	return new DashBoard(person);
    }

}
