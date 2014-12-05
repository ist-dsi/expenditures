/*
 * @(#)MissionOrganizationAction.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.mission.presentationTier.action;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jvstm.cps.ConsistencyException;
import module.mission.domain.MissionSystem;
import module.mission.domain.util.FunctionDelegationBean;
import module.mission.domain.util.SearchUnitMemberPresence;
import module.mission.presentationTier.dto.SearchMissionsDTO;
import module.organization.domain.Accountability;
import module.organization.domain.AccountabilityType;
import module.organization.domain.FunctionDelegation;
import module.organization.domain.OrganizationalModel;
import module.organization.domain.Party;
import module.organization.domain.Person;
import module.organization.domain.Unit;

import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.presentationTier.component.OrganizationChart;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;

@StrutsFunctionality(app = MissionProcessAction.class, path = "missionOrganization", titleKey = "link.sideBar.organization")
@Mapping(path = "/missionOrganization")
/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class MissionOrganizationAction extends BaseAction {

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final ActionForward forward = super.execute(mapping, form, request, response);
//        OrganizationModelAction.addHeadToLayoutContext(request);
        return forward;
    }

    @EntryPoint
    public ActionForward showPerson(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final User currentUser = Authenticate.getUser();
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
        final Collection<Accountability> workingPlaceAccountabilities =
                person.getParentAccountabilities(missionSystem.getAccountabilityTypesRequireingAuthorization());
        request.setAttribute("workingPlaceAccountabilities", workingPlaceAccountabilities);

        final Collection<Accountability> authorityAccountabilities =
                person.getParentAccountabilities(missionSystem.getAccountabilityTypesThatAuthorize());
        request.setAttribute("authorityAccountabilities", authorityAccountabilities);

        return forward("/mission/showPerson.jsp");
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

        final Collection<Accountability> authorityAccountabilities =
                sortChildren(unit.getChildrenAccountabilities(missionSystem.getAccountabilityTypesThatAuthorize()));
        request.setAttribute("authorityAccountabilities", authorityAccountabilities);

        final Collection<Accountability> workerAccountabilities =
                sortChildren(unit.getChildrenAccountabilities(missionSystem.getAccountabilityTypesRequireingAuthorization()));
        request.setAttribute("workerAccountabilities", workerAccountabilities);

        return forward("/mission/showUnit.jsp");
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

    public ActionForward addUnitWithResumedAuthorizations(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Unit unit = getDomainObject(request, "unitId");
        MissionSystem.getInstance().addUnitWithResumedAuthorizations(unit);
        return showUnit(unit, request);
    }

    public ActionForward removeUnitWithResumedAuthorizations(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Unit unit = getDomainObject(request, "unitId");
        MissionSystem.getInstance().removeUnitWithResumedAuthorizations(unit);
        return showUnit(unit, request);
    }

    public ActionForward showDelegationsForAuthorization(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Accountability accountability = getDomainObject(request, "authorizationId");
        return showDelegationsForAuthorization(request, accountability);
    }

    public ActionForward showDelegationsForAuthorization(final HttpServletRequest request, final Accountability accountability) {
        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");
        request.setAttribute("sortBy", (sortBy == null) ? "parentUnitName" : sortBy);
        request.setAttribute("order", (order == null) ? "asc" : order);
        request.setAttribute("accountability", accountability);

        Comparator<FunctionDelegation> comparator;
        if (StringUtils.equals(sortBy, "childPartyName")) {
            comparator = FunctionDelegation.COMPARATOR_BY_DELEGATEE_CHILD_PARTY_NAME;
        } else {
            comparator = FunctionDelegation.COMPARATOR_BY_DELEGATEE_PARENT_UNIT_NAME;
        }
        if (StringUtils.equals(order, "desc")) {
            comparator = ComparatorUtils.reversedComparator(comparator);
        }
        TreeSet<FunctionDelegation> functionDelegationDelegated = new TreeSet<FunctionDelegation>(comparator);
        functionDelegationDelegated.addAll(accountability.getFunctionDelegationDelegated());
        request.setAttribute("functionDelegationDelegated", functionDelegationDelegated);

        return forward("/mission/showDelegationForAuthorization.jsp");
    }

    public ActionForward prepareAddDelegationsForAuthorization(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Accountability accountability = getDomainObject(request, "authorizationId");
        final FunctionDelegationBean functionDelegationBean = new FunctionDelegationBean(accountability);

        request.setAttribute("functionDelegationBean", functionDelegationBean);
        return forward("/mission/addDelegationForAuthorization.jsp");
    }

    public ActionForward addDelegationsForAuthorization(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final FunctionDelegationBean functionDelegationBean = getRenderedObject();
        final Accountability accountability = functionDelegationBean.getAccountability();
        final Unit unit = functionDelegationBean.getUnit();
        final Person person = functionDelegationBean.getPerson();
        final LocalDate beginDate = functionDelegationBean.getBeginDate();
        final LocalDate endDate = functionDelegationBean.getEndDate();

        try {
            FunctionDelegation.create(accountability, unit, person, beginDate, endDate);
        } catch (DomainException ex) {
            addLocalizedMessage(request, ex.getLocalizedMessage());
            request.setAttribute("authorizationId", accountability.getExternalId());
            return prepareAddDelegationsForAuthorization(mapping, form, request, response);
        } catch (ConsistencyException exc) {
            displayConsistencyException(exc, request);
            request.setAttribute("authorizationId", accountability.getExternalId());
            return prepareAddDelegationsForAuthorization(mapping, form, request, response);
        }
        return showDelegationsForAuthorization(request, accountability);
    }

    public ActionForward prepareEditDelegation(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final FunctionDelegation functionDelegation = getDomainObject(request, "functionDelegationId");
        final FunctionDelegationBean functionDelegationBean = new FunctionDelegationBean(functionDelegation);

        request.setAttribute("functionDelegationBean", functionDelegationBean);
        return forward("/mission/editDelegation.jsp");
    }

    public ActionForward editDelegation(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final FunctionDelegationBean functionDelegationBean = getRenderedObject("functionDelegationBean");
        final FunctionDelegation functionDelegation = functionDelegationBean.getFunctionDelegation();
        final LocalDate beginDate = functionDelegationBean.getBeginDate();
        final LocalDate endDate = functionDelegationBean.getEndDate();

        try {
            functionDelegation.edit(beginDate, endDate);
        } catch (DomainException ex) {
            addLocalizedMessage(request, ex.getLocalizedMessage());
            request.setAttribute("functionDelegationId", functionDelegation.getExternalId());
            return prepareEditDelegation(mapping, form, request, response);
        } catch (ConsistencyException exc) {
            displayConsistencyException(exc, request);
            request.setAttribute("functionDelegationId", functionDelegation.getExternalId());
            return prepareEditDelegation(mapping, form, request, response);
        }

        return showDelegationsForAuthorization(request, functionDelegationBean.getAccountability());
    }

    public ActionForward removeDelegation(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final FunctionDelegation functionDelegation = getDomainObject(request, "functionDelegationId");
        Accountability accountabilityDelegator = functionDelegation.getAccountabilityDelegator();
        functionDelegation.delete();

        return showDelegationsForAuthorization(request, accountabilityDelegator);
    }

    public ActionForward viewPresences(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        SearchUnitMemberPresence searchUnitMemberPresence = getRenderedObject();
        final Unit unit;
        final boolean doSearch;
        if (searchUnitMemberPresence == null) {
            unit = getDomainObject(request, "unitId");
            searchUnitMemberPresence = new SearchUnitMemberPresence(unit);
            doSearch = false;
        } else {
            unit = searchUnitMemberPresence.getUnit();
            doSearch = true;
        }

        if (!hasPermission(unit)) {
            addLocalizedMessage(request, BundleUtil.getString("resources/MissionResources", "label.not.authorized"));
            return showUnit(unit, request);
        }

        request.setAttribute("searchUnitMemberPresence", searchUnitMemberPresence);

        if (doSearch) {
            final Set<Person> people = searchUnitMemberPresence.search();
            request.setAttribute("people", people);
        }

        return forward("/mission/viewPresences.jsp");
    }

    public static boolean hasPermission(final Unit unit) {
        final User user = Authenticate.getUser();
        if (user == null) {
            return false;
        }
        if (RoleType.MANAGER.group().isMember(user)) {
            return true;
        }
        final Person person = user == null ? null : user.getPerson();
        if (person != null) {
            final Set<AccountabilityType> accountabilityTypesThatAuthorize =
                    MissionSystem.getInstance().getAccountabilityTypesThatAuthorize();
            for (final Accountability accountability : person.getParentAccountabilitiesSet()) {
                final AccountabilityType accountabilityType = accountability.getAccountabilityType();
                if (accountabilityTypesThatAuthorize.contains(accountabilityType)) {
                    final Party authorization = accountability.getParent();
                    if (hasPermissionForParents(authorization, unit)) {
                        return true;
                    }
                }
            }
            if (user.getExpenditurePerson() != null && unit.getExpenditureUnit() != null) {
                if (user.getExpenditurePerson().getObservableUnitsSet().contains(unit.getExpenditureUnit())) {
                    return true;
                }
            }
        }

        final Collection<Party> parents = unit.getParents(MissionSystem.getInstance().getAccountabilityTypesForUnits());
        for (final Party party : parents) {
            if (party.isUnit()) {
                final Unit parent = (Unit) party;
                if (hasPermission(parent)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasPermissionForParents(final Party authorization, final Unit unit) {
        if (authorization == unit) {
            return true;
        }
        final OrganizationalModel organizationalModel = MissionSystem.getInstance().getOrganizationalModel();
        for (final Accountability accountability : unit.getParentAccountabilitiesSet()) {
            final AccountabilityType accountabilityType = accountability.getAccountabilityType();
            if (organizationalModel.getAccountabilityTypesSet().contains(accountabilityType)) {
                final Party parent = accountability.getParent();
                if (parent.isUnit() && hasPermissionForParents(authorization, (Unit) parent)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ActionForward searchMission(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personId");
        SearchMissionsDTO searchMissions = new SearchMissionsDTO();
        searchMissions.setParticipant(person);
        searchMissions.setProcessNumber("");
        return new SearchMissionsAction().search(searchMissions, request);
    }

}
