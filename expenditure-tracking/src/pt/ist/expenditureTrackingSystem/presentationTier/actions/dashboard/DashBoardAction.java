package pt.ist.expenditureTrackingSystem.presentationTier.actions.dashboard;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myorg.presentationTier.actions.BaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.Strings;

@Mapping(path = "/dashBoard")
public class DashBoardAction extends BaseAction {

    public ActionForward order(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws Exception {

	Person person = Person.getLoggedPerson();

	String column1 = request.getParameter("column1");
	String column2 = request.getParameter("column2");
	String column3 = request.getParameter("column3");

	person.getDashBoard().edit(getStrings(column1), getStrings(column2), getStrings(column3));
	return null;
    }

    private Strings getStrings(String column1) {
	String[] split = column1.substring(0, column1.length()).split(",");
	List<String> stringList = new ArrayList<String>();
	for (String string : split) {
	    if (string.length() > 0) {
		stringList.add(string);
	    }
	}
	Strings strings = new Strings(stringList);
	return strings;
    }

}
