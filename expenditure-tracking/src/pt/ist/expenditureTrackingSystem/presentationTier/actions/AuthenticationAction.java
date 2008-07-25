package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate;
import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping( path="/authenticationAction" )
@Forwards( { @Forward(name="forward", path="/home.do?method=firstPage", redirect=true) } )
public class AuthenticationAction extends BaseAction {

    public final ActionForward login(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response)
    		throws Exception {
	final String username = getAttribute(request, "username");
	final String password = getAttribute(request, "password");
	final User user = Authenticate.authenticate(username, password);
	final HttpSession httpSession = request.getSession();
	httpSession.setAttribute(SetUserViewFilter.USER_SESSION_ATTRIBUTE, user);	
	return mapping.findForward("forward");
    }

    public final ActionForward logout(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response)
		throws Exception {
	UserView.setUser(null);
	final HttpSession httpSession = request.getSession();
	httpSession.removeAttribute(SetUserViewFilter.USER_SESSION_ATTRIBUTE);
	return mapping.findForward("forward");
    }

}
