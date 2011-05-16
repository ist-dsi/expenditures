package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myorg.domain.VirtualHost;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValues;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValuesArray;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/expenditureConfiguration")
public class ExpenditureConfigurationAction extends BaseAction {

    public ActionForward viewConfiguration(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return forward(request, "/expenditureConfiguration.jsp");
    }

    public ActionForward saveConfiguration(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final Set<SearchProcessValues> valuesToSet = new HashSet<SearchProcessValues>();
	for (final SearchProcessValues values : SearchProcessValues.values()) {
	    final String parameter = request.getParameter(values.name());
	    if ("on".equals(parameter)) {
		valuesToSet.add(values);
	    }
	}
	final SearchProcessValuesArray array = SearchProcessValuesArray.importFromString(valuesToSet);

	final String acquisitionCreationWizardJsp = request.getParameter("acquisitionCreationWizardJsp");
	final String institutionalProcessNumberPrefix = request.getParameter("institutionalProcessNumberPrefix");

	ExpenditureTrackingSystem.getInstance().saveConfiguration(institutionalProcessNumberPrefix, acquisitionCreationWizardJsp, array);

	return viewConfiguration(mapping, form, request, response);
    }

    public ActionForward createNewSystem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	ExpenditureTrackingSystem.createSystem(virtualHost);

	return viewConfiguration(mapping, form, request, response);
    }

    public ActionForward useSystem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final ExpenditureTrackingSystem expenditureTrackingSystem = getDomainObject(request, "systemId");
	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	expenditureTrackingSystem.setForVirtualHost(virtualHost);

	return viewConfiguration(mapping, form, request, response);
    }

}
