package pt.ist.expenditureTrackingSystem.presentationTier.actions.organization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping( path="/organization" )
@Forwards( {
    @Forward(name="view.organization", path="/organization/viewOrganization.jsp")
} )
public class OrganizationAction extends BaseAction {

    private static final Context CONTEXT = new Context("organization");

    @Override
    protected Context getContextModule() {
	return CONTEXT;
    }

    public final ActionForward viewOrganization(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Unit unit = getDomainObject(request, "unitOid");
	request.setAttribute("unit", unit);
	return mapping.findForward("view.organization");
    }

}
