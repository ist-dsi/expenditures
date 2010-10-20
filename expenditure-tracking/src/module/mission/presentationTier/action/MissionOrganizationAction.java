package module.mission.presentationTier.action;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.mission.domain.MissionSystem;
import module.organization.domain.Accountability;
import module.organization.domain.AccountabilityType;
import module.organization.domain.Party;
import module.organization.domain.Person;
import module.organization.domain.Unit;
import module.organization.presentationTier.actions.OrganizationModelAction;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.presentationTier.actions.ContextBaseAction;
import myorg.presentationTier.component.OrganizationChart;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/missionOrganization")
public class MissionOrganizationAction extends ContextBaseAction {

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws Exception {
	final ActionForward forward = super.execute(mapping, form, request, response);
	OrganizationModelAction.addHeadToLayoutContext(request);
	return forward;
    }

    public ActionForward showPerson(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final User currentUser = UserView.getCurrentUser();
	final Person person = currentUser.getPerson();
	return showPerson(person, request);
    }

    public ActionForward showPersonById(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final Person person = getDomainObject(request, "personId");
	return showPerson(person, request);
    }

    public ActionForward showPerson(final Person person, final HttpServletRequest request) {
	request.setAttribute("person", person);

	final MissionSystem missionSystem = MissionSystem.getInstance();
	final Collection<Accountability> workingPlaceAccountabilities = person.getParentAccountabilities(missionSystem.getAccountabilityTypesRequireingAuthorization());
	request.setAttribute("workingPlaceAccountabilities", workingPlaceAccountabilities);

	final Collection<Accountability> authorityAccountabilities = person.getParentAccountabilities(missionSystem.getAccountabilityTypesThatAuthorize());
	request.setAttribute("authorityAccountabilities", authorityAccountabilities);

	return forward(request, "/mission/showPerson.jsp");
    }

    public ActionForward showUnitById(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final Unit unit = getDomainObject(request, "unitId");
	return showUnit(unit, request);
    }

    public ActionForward showUnit(final Unit unit, final HttpServletRequest request) {
	request.setAttribute("unit", unit);

	final MissionSystem missionSystem = MissionSystem.getInstance();
	final Collection<AccountabilityType> accountabilityTypes = missionSystem.getAccountabilityTypesForUnits();
	final Collection<Unit> parents = sortUnit(unit.getParents(accountabilityTypes));
	final Collection<Unit> children = sortUnit(unit.getChildren(accountabilityTypes));
	OrganizationChart<Unit> chart = new OrganizationChart<Unit>(unit, parents, children, 3);
	request.setAttribute("chart", chart);

	final Collection<Accountability> authorityAccountabilities = sortChildren(unit.getChildrenAccountabilities(missionSystem.getAccountabilityTypesThatAuthorize()));
	request.setAttribute("authorityAccountabilities", authorityAccountabilities);

	final Collection<Accountability> workerAccountabilities = sortChildren(unit.getChildrenAccountabilities(missionSystem.getAccountabilityTypesRequireingAuthorization()));
	request.setAttribute("workerAccountabilities", workerAccountabilities);

	return forward(request, "/mission/showUnit.jsp");
    }

    private Collection<Unit> sortUnit(final Collection<Party> parties) {
	final SortedSet<Unit> result = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME);
	result.addAll((Collection) parties);
	return result;
    }

    private Collection<Accountability> sortChildren(final Collection<Accountability> accountabilities) {
	final SortedSet<Accountability> result = new TreeSet<Accountability>(Accountability.COMPARATOR_BY_CHILD_PARTY_NAMES);
	result.addAll(accountabilities);
	return result;
    }

    public ActionForward addUnitWithResumedAuthorizations(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final Unit unit = getDomainObject(request, "unitId");
	MissionSystem.getInstance().addUnitWithResumedAuthorizations(unit);
	return showUnit(unit, request);
    }

    public ActionForward removeUnitWithResumedAuthorizations(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final Unit unit = getDomainObject(request, "unitId");
	MissionSystem.getInstance().removeUnitWithResumedAuthorizations(unit);
	return showUnit(unit, request);
    }
    

}
