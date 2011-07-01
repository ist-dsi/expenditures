package pt.ist.expenditureTrackingSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import myorg._development.PropertiesManager;
import myorg.domain.MyOrg;
import pt.ist.expenditureTrackingSystem.domain.DashBoard;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.Strings;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class DashBoardResetter {

    public static void init() {
	final String domainmodelPath = new File("build/WEB-INF/classes").getAbsolutePath();
	System.out.println("domainmodelPath: " + domainmodelPath);
	final File dir = new File(domainmodelPath);
	final List<String> urls = new ArrayList<String>();
	for (final File file : dir.listFiles()) {
	    if (file.isFile() && file.getName().endsWith(".dml")) {
		try {
		    urls.add(file.getCanonicalPath());
		} catch (IOException e) {
		    e.printStackTrace();
		    throw new Error(e);
		}
	    }
	}
	Collections.sort(urls);
	final String[] paths = new String[urls.size()];
	for (int i = 0; i < urls.size(); i++) {
	    paths[i] = urls.get(i);
	}

	Language.setDefaultLocale(new Locale("pt", "PT"));
	Language.setLocale(Language.getDefaultLocale());

	FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(paths));
    }

    public static void main(String[] args) {
	init();
	reset();
	System.out.println("Done.");
    }

    @Service
    private static void reset() {
	final ExpenditureTrackingSystem expenditureTrackingSystem = ExpenditureTrackingSystem.getInstance();
	for (final Person person : MyOrg.getInstance().getPeopleFromExpenditureTackingSystemSet()) {
	    final DashBoard dashBoard = person.getDashBoard();
	    dashBoard.setExpenditureTrackingSystem(expenditureTrackingSystem);
	    addWidget1("widgetUnreadComments", dashBoard);
	    addWidget3("widgetActivateEmailNotification", dashBoard);
	}
    }

    private static void addWidget1(final String widgetName, final DashBoard dashBoard) {
	if (!hasWidget(widgetName, dashBoard)) {
	    final List<String> column1 = new ArrayList<String>();
	    for (final String string : dashBoard.getColumn1()) {
		column1.add(string);
	    }
	    column1.add(widgetName);
	    dashBoard.setColumn1(new Strings(column1));
	}
    }

    private static void addWidget2(final String widgetName, final DashBoard dashBoard) {
	if (!hasWidget(widgetName, dashBoard)) {
	    final List<String> column2 = new ArrayList<String>();
	    for (final String string : dashBoard.getColumn2()) {
		column2.add(string);
	    }
	    column2.add(widgetName);
	    dashBoard.setColumn2(new Strings(column2));
	}
    }

    private static void addWidget3(final String widgetName, final DashBoard dashBoard) {
	if (!hasWidget(widgetName, dashBoard)) {
	    final List<String> column3 = new ArrayList<String>();
	    for (final String string : dashBoard.getColumn3()) {
		column3.add(string);
	    }
	    column3.add(widgetName);
	    dashBoard.setColumn3(new Strings(column3));
	}
    }

    private static boolean hasWidget(String string, DashBoard dashBoard) {
	return dashBoard.getColumn1().contains(string) || dashBoard.getColumn2().contains(string)
		|| dashBoard.getColumn3().contains(string);
    }

}