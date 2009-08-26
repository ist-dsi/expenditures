package pt.ist.expenditureTrackingSystem.presentationTier.actions.organization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.organization.domain.Party;
import module.organization.presentationTier.actions.OrganizationModelAction.PartyChart;
import myorg.domain.MyOrg;
import myorg.presentationTier.LayoutContext;
import myorg.presentationTier.component.OrganizationChart;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/connectUnits")
public class ConnectUnitsAction extends BaseAction {

    public static class UnitChart extends OrganizationChart<Object> {

	public UnitChart(final Collection<Unit> units) {
	    super(sortCollection(units), 1);
	}

	public UnitChart(final Unit unit) {
	    super(unit, collect(unit.getParentUnit()), sortCollection(unit.getSubUnitsSet()), 1);
	}

	public UnitChart(final Unit unit, final Collection<Unit> parents, final Collection<Unit> children) {
	    super(unit, (Collection) parents, (Collection)children, 1);
	}

	private static Collection collect(final Object object) {
	    final List<Object> result = new ArrayList<Object>(1);
	    result.add(object);
	    return result;
	}

	private static Collection sortCollection(final Collection<Unit> units) {
	    final List<Unit> result = new ArrayList<Unit>(units);
	    Collections.sort(result, Unit.COMPARATOR_BY_PRESENTATION_NAME);
	    return result;
	}

	public Unit getUnit() {
	    final Object object = getElement();
	    return object instanceof Unit ? ((Unit) object) : null;
	}
    }

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws Exception {
	final ActionForward forward = super.execute(mapping, form, request, response);
	final LayoutContext layoutContext = (LayoutContext) getContext(request);
	request.setAttribute("previousLayoutContextHead", layoutContext.getHead());
	layoutContext.addHead("/organization/layoutContext/head.jsp");
	return forward;
    }

    private static Collection collect(final Object object) {
	if (object != null) {
	    final List<Object> result = new ArrayList<Object>(1);
	    result.add(object);
	    return result;
	}
	return null;
    }

    private static Collection<Unit> sortCollection(final Collection<Unit> units) {
	final SortedSet<Unit> result = new TreeSet<Unit>(Unit.COMPARATOR_BY_PRESENTATION_NAME);
	result.addAll(units);
	return result;
    }

    public final ActionForward showUnits(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final MyOrg myOrg = MyOrg.getInstance();
	final Party party = getDomainObject(request, "partyId");
	final PartyChart partyChart = party == null ? new PartyChart((Set) myOrg.getTopUnitsSet(), 1) :
		new PartyChart(party, PartyChart.sortCollection((Collection) party.getParentUnits()), PartyChart.sortCollection((Collection) party.getChildUnits()), 1);
	request.setAttribute("partyChart", partyChart);

	final ExpenditureTrackingSystem expenditureTrackingSystem = myOrg.getExpenditureTrackingSystem();
	final Unit unit = getDomainObject(request, "unitId");
	final UnitChart unitChart = unit == null ?
		new UnitChart(((Set) expenditureTrackingSystem.getTopLevelUnitsSet())) : new UnitChart(unit, collect(unit.getParentUnit()), sortCollection(unit.getSubUnitsSet()));
	request.setAttribute("unitChart", unitChart);

	return forward(request, "/expenditureTrackingOrganization/connectUnits.jsp");
    }

    public final ActionForward disconnectParty(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final module.organization.domain.Unit unit = getDomainObject(request, "partyId");
	unit.removeExpenditureUnit();
	return showUnits(mapping, form, request, response);
    }

    public final ActionForward disconnectUnit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final Unit unit = getDomainObject(request, "unitId");
	unit.removeUnit();
	return showUnits(mapping, form, request, response);
    }

    public final ActionForward connect(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final module.organization.domain.Unit organizationUnit = getDomainObject(request, "partyId");
	final Unit unit = getDomainObject(request, "unitId");
	unit.setUnit(organizationUnit);
	return showUnits(mapping, form, request, response);
    }

    public final ActionForward disconnect(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final Unit unit = getDomainObject(request, "unitId");
	unit.removeUnit();
	return showUnits(mapping, form, request, response);
    }

}
