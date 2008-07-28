package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.Options;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping( path="/customize" )
@Forwards( {
    @Forward(name="show.options", path="/options/options.jsp")
} )
public class CustomizeAction extends BaseAction {

    private static final Context CONTEXT = new Context("options");

    @Override
    protected Context getContextModule() {
	return CONTEXT;
    }

    public final ActionForward showOptions(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final User user = UserView.getUser();
	final Person person = user.getPerson();
	final Options options = person.getOptions();
	request.setAttribute("options", options);
	return mapping.findForward("show.options");
    }

}
