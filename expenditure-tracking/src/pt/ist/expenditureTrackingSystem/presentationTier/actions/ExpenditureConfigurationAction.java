package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValues;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValuesArray;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/expenditureConfiguration")
public class ExpenditureConfigurationAction extends BaseAction {

    public final ActionForward viewConfiguration(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return forward(request, "/expenditureConfiguration.jsp");
    }

    public final ActionForward saveSelectedSearchProcessValues(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final Set<SearchProcessValues> valuesToSet = new HashSet<SearchProcessValues>();
	for (final SearchProcessValues values : SearchProcessValues.values()) {
	    final String parameter = request.getParameter(values.name());
	    if ("on".equals(parameter)) {
		valuesToSet.add(values);
	    }
	}
	final SearchProcessValuesArray array = SearchProcessValuesArray.importFromString(valuesToSet);
	ExpenditureTrackingSystem.getInstance().setSearchProcessValuesService(array);

	return viewConfiguration(mapping, form, request, response);
    }

}
