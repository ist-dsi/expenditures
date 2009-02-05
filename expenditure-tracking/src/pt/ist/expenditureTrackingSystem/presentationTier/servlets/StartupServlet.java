package pt.ist.expenditureTrackingSystem.presentationTier.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import myorg._development.PropertiesManager;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.MyOwnProcessesSearch;
import pt.ist.expenditureTrackingSystem.domain.PendingProcessesSearch;
import pt.ist.expenditureTrackingSystem.domain.SavedSearch;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.services.Service;

public class StartupServlet extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	String domainModelPath = getServletContext().getRealPath(getInitParameter("domainmodel"));
	// TODO : delete this file... integrate with myorg stuff
	//FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(domainModelPath));
	//ExpenditureTrackingSystem.initialize(FenixWebFramework.getConfig());

	final String managerUsernames = PropertiesManager.getProperty("manager.usernames");
//	Authenticate.initRole(RoleType.MANAGER, managerUsernames);
//	final String acquisitionCentralUsernames = PropertiesManager.getProperty("acquisitionCentral.usernames");
//	Authenticate.initRole(RoleType.ACQUISITION_CENTRAL, acquisitionCentralUsernames);
//	final String acquisitionCentralAdministratorUsernames = PropertiesManager
//		.getProperty("acquisitionCentralAdministrator.usernames");
//	Authenticate.initRole(RoleType.ACQUISITION_CENTRAL_MANAGER, acquisitionCentralAdministratorUsernames);
//	final String accountingManagerUsernames = PropertiesManager.getProperty("accountingAdministrator.usernames");
//	Authenticate.initRole(RoleType.ACCOUNTING_MANAGER, accountingManagerUsernames);
//	final String projectAccountingManagerUsernames = PropertiesManager.getProperty("accountingAdministrator.usernames");
//	Authenticate.initRole(RoleType.PROJECT_ACCOUNTING_MANAGER, projectAccountingManagerUsernames);
//	final String treasuryUsernames = PropertiesManager.getProperty("treasury.usernames");
//	Authenticate.initRole(RoleType.TREASURY, treasuryUsernames);
//	final String statisticsViewerUsernames = PropertiesManager.getProperty("statisticsViewer.usernames");
//	Authenticate.initRole(RoleType.STATISTICS_VIEWER, statisticsViewerUsernames);
	initSystemSearches();

    }

    @Service
    private synchronized void initSystemSearches() {
	if (ExpenditureTrackingSystem.getInstance().getSystemSearches().isEmpty()) {
	    new MyOwnProcessesSearch();
	    SavedSearch savedSearch = new PendingProcessesSearch();
	    for (Person person : ExpenditureTrackingSystem.getInstance().getPeople()) {
		person.setDefaultSearch(savedSearch);
	    }
	}
    }

}
