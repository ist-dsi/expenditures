package pt.ist.expenditureTrackingSystem.presentationTier.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import pt.ist.expenditureTrackingSystem._development.PropertiesManager;
import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.fenixWebFramework.FenixWebFramework;

public class StartupServlet extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	String domainModelPath = getServletContext().getRealPath(getInitParameter("domainmodel"));
	FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(domainModelPath));
	ExpenditureTrackingSystem.initialize(FenixWebFramework.getConfig());

	final String managerUsernames = PropertiesManager.getProperty("manager.usernames");
	Authenticate.initRole(RoleType.MANAGER, managerUsernames);
	final String acquisitionCentralUsernames = PropertiesManager.getProperty("acquisitionCentral.usernames");
	Authenticate.initRole(RoleType.ACQUISITION_CENTRAL, acquisitionCentralUsernames);
	final String acquisitionCentralAdministratorUsernames = PropertiesManager.getProperty("acquisitionCentralAdministrator.usernames");
	Authenticate.initRole(RoleType.ACQUISITION_CENTRAL_MANAGER, acquisitionCentralAdministratorUsernames);
	final String accountingUsernames = PropertiesManager.getProperty("acounting.usernames");
	Authenticate.initRole(RoleType.ACCOUNTABILITY, accountingUsernames);
	final String treasuryUsernames = PropertiesManager.getProperty("treasury.usernames");
	Authenticate.initRole(RoleType.TREASURY, treasuryUsernames);
    }

}
