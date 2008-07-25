package pt.ist.expenditureTrackingSystem.presentationTier.actions.organization;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping( path="/organization" )
@Forwards( {
    @Forward(name="view.organization", path="/organization/viewOrganization.jsp"),
    @Forward(name="edit.unit", path="/organization/editUnit.jsp")
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
	return viewOrganization(mapping, request, unit);
    }

    public final ActionForward viewOrganization(final ActionMapping mapping, final HttpServletRequest request,
	    final Unit unit) throws Exception {
	request.setAttribute("unit", unit);
	final Set<Unit> units = unit == null ? ExpenditureTrackingSystem.getInstance().getTopLevelUnitsSet() : unit.getSubUnitsSet();
	request.setAttribute("units", units);
	return mapping.findForward("view.organization");
    }

    //<html:link action="/organization.do?method=createNewUnit" paramId="unitOid" paramName="unit" paramProperty="OID">
    public final ActionForward createNewUnit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Unit unit = getDomainObject(request, "unitOid");
	final Unit newUnit = Unit.createNewUnit(unit);
	return editUnit(mapping, request, newUnit);
    }

    private ActionForward editUnit(ActionMapping mapping, HttpServletRequest request, Unit unit) {
	request.setAttribute("unit", unit);
	return mapping.findForward("edit.unit");
    }

    public final ActionForward editUnit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Unit unit = getDomainObject(request, "unitOid");
	return editUnit(mapping, request, unit);
    }

    public final ActionForward deleteUnit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Unit unit = getDomainObject(request, "unitOid");
	final Unit parentUnit = unit.getParentUnit();
	unit.delete();
	return viewOrganization(mapping, request, parentUnit);
    }

}
