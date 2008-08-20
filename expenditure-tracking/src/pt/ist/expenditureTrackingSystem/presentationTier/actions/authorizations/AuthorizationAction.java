package pt.ist.expenditureTrackingSystem.presentationTier.actions.authorizations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.authorizations.DelegatedAuthorization;
import pt.ist.expenditureTrackingSystem.domain.dto.AuthorizationBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/authorizations")
@Forwards( { @Forward(name = "view.authorizations", path = "/authorizations/viewAuthorizations.jsp"),
	@Forward(name = "delegate.authorization", path = "/authorizations/delegateAuthorization.jsp"),
	@Forward(name = "authorization.details", path = "/authorizations/authorizationDetails.jsp") })
public class AuthorizationAction extends BaseAction {

    private static final Context CONTEXT = new Context("authorizations");

    @Override
    protected Context getContextModule() {
	return CONTEXT;
    }

    public ActionForward viewAuthorizations(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	User user = UserView.getUser();
	Person person = user != null ? user.getPerson() : null;
	request.setAttribute("person", person);

	return mapping.findForward("view.authorizations");
    }

    public ActionForward delegateAuthorization(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	Authorization authorization = getDomainObject(request, "authorizationOID");
	Person person = getLoggedPerson();

	if (authorization.getPerson() == person && authorization.getCanDelegate()) {
	    AuthorizationBean bean = new AuthorizationBean(authorization);
	    request.setAttribute("bean", bean);
	} else {
	    addErrorMessage("label.unable.to.delegate.that.action", "EXPENDITURE_RESOURCE");
	}

	return mapping.findForward("delegate.authorization");
    }

    public ActionForward createDelegation(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	AuthorizationBean bean = getRenderedObject("bean");
	try {
	    DelegatedAuthorization.delegate(bean.getAuthorization(), bean.getPerson(), bean.getCanDelegate());
	} catch (DomainException e) {
	    addErrorMessage(e.getMessage(), "EXPENDITURE_RESOURCE");
	    request.setAttribute("bean", bean);
	    return mapping.findForward("delegate.authorization");
	}
	return viewAuthorizations(mapping, form, request, response);
    }

    public ActionForward viewAuthorizationDetails(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	Authorization authorization = getDomainObject(request, "authorizationOID");
	request.setAttribute("authorization", authorization);
	return mapping.findForward("authorization.details");
    }

    public ActionForward revokeAuthorization(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	DelegatedAuthorization authorizationToRevoke = getDomainObject(request, "revokeAuthorizationOID");
	
	try {
	    authorizationToRevoke.revoke();
	} catch (DomainException e) {
	    addErrorMessage(e.getMessage(), "EXPENDITURE_RESOURCES");
	}

	return viewAuthorizationDetails(mapping, form, request, response);
    }

}
