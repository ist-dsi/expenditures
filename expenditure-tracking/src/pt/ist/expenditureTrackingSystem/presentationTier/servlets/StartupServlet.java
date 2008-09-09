package pt.ist.expenditureTrackingSystem.presentationTier.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import pt.ist.expenditureTrackingSystem._development.PropertiesManager;
import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.fenixWebFramework.FenixWebFramework;

public class StartupServlet extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	String domainModelPath = getServletContext().getRealPath(getInitParameter("domainmodel"));
	FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(domainModelPath));
	ExpenditureTrackingSystem.initialize(FenixWebFramework.getConfig());
	final String managerUsername = PropertiesManager.getProperty("manager.usernames");
	Authenticate.init(managerUsername);
    }

}
