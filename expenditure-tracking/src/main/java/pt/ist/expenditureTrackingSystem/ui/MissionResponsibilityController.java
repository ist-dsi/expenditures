package pt.ist.expenditureTrackingSystem.ui;

import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.mission.domain.MissionSystem;
import module.mission.domain.util.AuthorizationChain;
import module.mission.domain.util.FindUnitMemberPresence;
import module.mission.domain.util.ParticipantAuthorizationChain;
import module.organization.domain.Accountability;
import module.organization.domain.AccountabilityType;
import module.organization.domain.OrganizationalModel;
import module.organization.domain.Party;
import module.organization.domain.Person;
import module.organization.domain.Unit;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.StringNormalizer;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@SpringApplication(group = "logged", path = "expenditure-tracking", title = "title.mission.manage.missions",
        hint = "expenditure-tracking")
@SpringFunctionality(app = MissionResponsibilityController.class, title = "title.mission.manage.missions")
@RequestMapping("/expenditure-tracking/manageMissions")
public class MissionResponsibilityController {

    @RequestMapping
    public String search(@RequestParam(required = false) String partyId, final Model model) throws Exception {
        model.addAttribute("partyId", partyId);

        if ((partyId != null && partyId != "")) {

            final DomainObject o = FenixFramework.getDomainObject(partyId);
            if (o instanceof User) {
                model.addAttribute("selectedUser", o);
                model.addAttribute("linha", "");
                model.addAttribute("er", true);
                return showPerson((User) o, model);
            } else if (o instanceof module.organization.domain.Unit) {
                model.addAttribute("notActive", false);
                model.addAttribute("notAutorize", false);
                model.addAttribute("selectedUnit", o);
                model.addAttribute("notAutorize", false);
                return showUnit((Unit) o, model);
            } else if (o instanceof Person) {
                User user = ((Person) o).getUser();
                model.addAttribute("selectedUser", o);
                model.addAttribute("linha", "");
                model.addAttribute("er", true);
                return showPerson(user, model);
            }
        }
        return "redirect:/mission/missionOrganization";
    }

    @RequestMapping(value = "/populate/json", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public @ResponseBody String populate(@RequestParam(required = false, value = "term") String term, final Model model) {
        final JsonArray result = new JsonArray();
        final String trimmedValue = term.trim();
        final String[] input = StringNormalizer.normalize(trimmedValue).split(" ");
        findUnits(result, input);
        findPeople(result, input);
        return result.toString();
    }

    @RequestMapping(value = "/user/json", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public @ResponseBody String user(@RequestParam(required = false, value = "term") String term, final Model model) {
        final JsonArray result = new JsonArray();
        final String trimmedValue = term.trim();
        final String[] input = StringNormalizer.normalize(trimmedValue).split(" ");
        findPeople(result, input);
        return result.toString();
    }

    @RequestMapping(value = "/unit/json", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public @ResponseBody String unit(@RequestParam(required = false, value = "term") String term, final Model model) {
        final JsonArray result = new JsonArray();
        final String trimmedValue = term.trim();
        final String[] input = StringNormalizer.normalize(trimmedValue).split(" ");
        findUnits(result, input);
        return result.toString();
    }

    private void findPeople(JsonArray result, String[] input) {
        findPeople(input).forEach(u -> addPersonToJson(result, u));
    }

    private void addPersonToJson(JsonArray result, User u) {
        final JsonObject o = new JsonObject();
        o.addProperty("id", u.getExternalId());
        o.addProperty("name", u.getPresentationName());
        result.add(o);
    }

    private void findUnits(JsonArray result, String[] input) {
        final OrganizationalModel model = MissionSystem.getInstance().getOrganizationalModel();
        final Set<AccountabilityType> typesSet = model.getAccountabilityTypesSet();
        model.getPartiesSet().forEach(p -> findUnits(result, input, typesSet, p));
    }

    private void findUnits(JsonArray result, String[] input, final Set<AccountabilityType> typesSet, Party p) {
        if (p.isUnit()) {
            final module.organization.domain.Unit unit = (module.organization.domain.Unit) p;

            if (hasMatch(input, StringNormalizer.normalize(unit.getPresentationName()).toLowerCase())) {
                final JsonObject o = new JsonObject();
                o.addProperty("id", unit.getExternalId());
                o.addProperty("name", unit.getPresentationName());
                result.add(o);
            }
            for (final Accountability a : unit.getChildAccountabilitiesSet()) {
                if (a.isActiveNow() && typesSet.contains(a.getAccountabilityType())) {
                    findUnits(result, input, typesSet, a.getChild());
                }
            }
        }
    }

    private Stream<User> findPeople(String[] input) {
        final Stream<User> users = Bennu.getInstance().getUserSet().stream();
        return users.filter(u -> match(input, u));
    }

    private boolean match(String[] values, User u) {

        return (values.length == 1 && u.getUsername().equalsIgnoreCase(values[0]))
                || (u.getProfile() != null && hasMatch(values, StringNormalizer.normalize(u.getProfile().getFullName())
                        .toLowerCase()));
    }

    private boolean hasMatch(final String[] input, final String unitNameParts) {

        for (final String namePart : input) {
            if (unitNameParts.indexOf(namePart) == -1) {
                return false;
            }
        }
        return true;
    }

    public String showPerson(final User user, final Model model) throws Exception {
        final MissionSystem missionSystem = MissionSystem.getInstance();
        final module.organization.domain.Person person = user.getPerson();
        final Collection<Accountability> workingPlaceAccountabilities =
                person.getParentAccountabilities(missionSystem.getAccountabilityTypesRequireingAuthorization());
        final Collection<Accountability> authorityAccountabilities =
                person.getParentAccountabilities(missionSystem.getAccountabilityTypesThatAuthorize());

        model.addAttribute("workingPlaceAccountabilities", workingPlaceAccountabilities);
        model.addAttribute("authorityAccountabilities", authorityAccountabilities);
        model.addAttribute("selectedUser", user);
        return "expenditure-tracking/showPerson";
    }

    public String showUnit(final Unit unit, final Model model) {
        final MissionSystem missionSystem = MissionSystem.getInstance();
        final Collection<Accountability> authorityAccountabilities =
                sortChildren(unit.getChildrenAccountabilities(missionSystem.getAccountabilityTypesThatAuthorize()));
        final Collection<Accountability> workerAccountabilities =
                sortChildren(unit.getChildrenAccountabilities(missionSystem.getAccountabilityTypesRequireingAuthorization()));

        model.addAttribute("unit", unit);
        model.addAttribute("authorityAccountabilities", authorityAccountabilities);
        model.addAttribute("workerAccountabilities", workerAccountabilities);
        model.addAttribute("selectedUnit", unit);

        return "expenditure-tracking/showUnit";
    }

    private Collection<Accountability> sortChildren(final Collection<Accountability> accountabilities) {
        final SortedSet<Accountability> result = new TreeSet<Accountability>(Accountability.COMPARATOR_BY_CHILD_PARTY_NAMES);
        result.addAll(accountabilities);
        return result;
    }

    @RequestMapping(value = "/viewPresences", method = RequestMethod.GET)
    public String viewPresences(@RequestParam(required = false, value = "unitId") String unitId, Model model) {
        final Unit unit;

        unit = FenixFramework.getDomainObject(unitId);
        if (!hasPermission(unit)) {
            model.addAttribute("notAutorize", true);
            return showUnit(unit, model);
        }
        return redirect(unitId, "missionOrganization", "viewPresences", "unitId");
    }

    @RequestMapping(value = "/loadAuthorizationTypes", method = RequestMethod.GET)
    public @ResponseBody Collection<AccountabilityType> loadAuthorizationTypes() throws Exception {
        final Collection<AccountabilityType> accountabilityTypes =
                MissionSystem.getInstance().getAccountabilityTypesThatAuthorize();
        return accountabilityTypes;
    }

    @RequestMapping(value = "/Presences", method = RequestMethod.POST)
    public String Presences(@RequestParam(required = false, value = "unitId") String unitId,
            FindUnitMemberPresence searchUnitMemberPresence, Model model) {
        final Unit unit;

        unit = FenixFramework.getDomainObject(unitId);
        searchUnitMemberPresence =
                new FindUnitMemberPresence(unit, searchUnitMemberPresence.getDay(),
                        searchUnitMemberPresence.getAccountabilityTypes(), searchUnitMemberPresence.isIncludeSubUnits(),
                        searchUnitMemberPresence.isOnMission());
        model.addAttribute("selectedUnit", unit);
        model.addAttribute("searchUnitMemberPresence", searchUnitMemberPresence);
        final Set<Person> people = searchUnitMemberPresence.search();
        model.addAttribute("accountabilityTypesList", searchUnitMemberPresence.getAccountabilityTypes());
        model.addAttribute("unitId", unitId);
        model.addAttribute("people", people);
        return "expenditure-tracking/viewPresences";
    }

    @RequestMapping(value = "/prepareRelationshipType/{accountId}", method = RequestMethod.GET)
    public String prepareRelationshipType(@PathVariable String accountId, final Model model) throws Exception {
        final MissionSystem mission = MissionSystem.getInstance();
        final DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
        final LocalDate local = new LocalDate();
        final SortedSet<AccountabilityType> set = new TreeSet<>(AccountabilityType.COMPARATORY_BY_NAME);
        final Set<AccountabilityType> workingPlaceAccountabilities = mission.getAccountabilityTypesRequireingAuthorization();
        set.addAll(workingPlaceAccountabilities);
        final Set<AccountabilityType> authorityAccountabilities = mission.getAccountabilityTypesThatAuthorize();
        set.addAll(authorityAccountabilities);
        model.addAttribute("accountabilityTypes", set);
        model.addAttribute("invalidData", false);
        model.addAttribute("notActive", false);
        model.addAttribute("iniDate", local.toString(formatter));
        if (FenixFramework.getDomainObject(accountId) instanceof User) {
            model.addAttribute("user", FenixFramework.getDomainObject(accountId));
        } else if (FenixFramework.getDomainObject(accountId) instanceof Unit) {
            model.addAttribute("unit", FenixFramework.getDomainObject(accountId));
        }
        if (ExpenditureTrackingSystem.getInstance().getAcquisitionsUnitManagerGroup().isMember(Authenticate.getUser())) {
            model.addAttribute("Allowed", true);
            return "expenditure-tracking/addMissionResponsability";
        }

        return search(accountId, model);
    }

    @RequestMapping(value = "/prepareAddSubUnit/{partyId}", method = RequestMethod.GET)
    public String prepareAddSubUnit(@PathVariable String partyId, final Model model) throws Exception {

        final DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
        final LocalDate local = new LocalDate();

        model.addAttribute("invalidData", false);
        model.addAttribute("iniDate", local.toString(formatter));
        model.addAttribute("types", MissionSystem.getInstance().getAccountabilityTypesForUnits());

        if (FenixFramework.getDomainObject(partyId) instanceof Unit) {
            model.addAttribute("notActive", false);
            Unit u = FenixFramework.getDomainObject(partyId);
            model.addAttribute("unit", u);

        }
        if (ExpenditureTrackingSystem.getInstance().getAcquisitionsUnitManagerGroup().isMember(Authenticate.getUser())) {
            model.addAttribute("Allowed", true);
            return "expenditure-tracking/addSubUnit";
        }

        return search(partyId, model);
    }

    @RequestMapping(value = "/prepareDelegateForAuthorization/{accountId}", method = RequestMethod.GET)
    public String prepareDelegateForAuthorization(@PathVariable String accountId, final Model model) throws Exception {
        final MissionSystem mission = MissionSystem.getInstance();
        final DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
        final LocalDate local = new LocalDate();
        model.addAttribute("accountabilityTypes", mission.getAccountabilityTypesThatAuthorize());
        model.addAttribute("invalidData", false);
        model.addAttribute("iniDate", local.toString(formatter));
        if (FenixFramework.getDomainObject(accountId) instanceof User) {
            model.addAttribute("user", FenixFramework.getDomainObject(accountId));
        } else if (FenixFramework.getDomainObject(accountId) instanceof Unit) {
            model.addAttribute("unit", FenixFramework.getDomainObject(accountId));
        }
        if (ExpenditureTrackingSystem.getInstance().getAcquisitionsUnitManagerGroup().isMember(Authenticate.getUser())) {
            model.addAttribute("Allowed", true);
            return "expenditure-tracking/addMissionResponsability";
        }

        return search(accountId, model);
    }

    @RequestMapping(value = "/addUnitWithResumedAuthorizations/{unitId}", method = RequestMethod.GET)
    public String addUnitWithResumedAuthorizations(@PathVariable String unitId, final Model model) throws Exception {
        final Unit unit = FenixFramework.getDomainObject(unitId);
        MissionSystem.getInstance().addUnitWithResumedAuthorizations(unit);
        model.addAttribute("notAutorize", false);
        return showUnit(unit, model);
    }

    @RequestMapping(value = "/removeUnitWithResumedAuthorizations/{unitId}", method = RequestMethod.GET)
    public String removeUnitWithResumedAuthorizations(@PathVariable String unitId, final Model model) throws Exception {
        final Unit unit = FenixFramework.getDomainObject(unitId);
        MissionSystem.getInstance().removeUnitWithResumedAuthorizations(unit);
        model.addAttribute("notAutorize", false);
        return showUnit(unit, model);
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

    @RequestMapping(value = "/searchMission/{personId}", method = RequestMethod.GET)
    public String searchMission(@PathVariable String personId, HttpServletRequest request, final HttpServletResponse response,
            Model model) throws Exception {
        return redirect(personId, "missionOrganization", "searchMission", "personId");
    }

    private String redirect(final String id, final String action, final String method, final String param) {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final String contextPath = request.getContextPath();
        final String path = "/" + action + ".do?method=" + method + "&" + param + "=" + id;
        final String safePath =
                path + "&" + GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME + "="
                        + GenericChecksumRewriter.calculateChecksum(contextPath + path, request.getSession());
        return "redirect:" + safePath;
    }

    @RequestMapping(value = "/showDelegationsForAuthorization/{authorizationId}", method = RequestMethod.GET)
    public String showDelegationsForAuthorization(@PathVariable String authorizationId, Model model) {
        return redirect(authorizationId, "missionOrganization", "showDelegationsForAuthorization", "authorizationId");
    }

    @RequestMapping(value = "{userId}/authorizationChain/json", method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public @ResponseBody String authorizationChain(@PathVariable String userId, @RequestParam(required = false) String param,
            final Model model) {
        JsonArray result = new JsonArray();
        User user = FenixFramework.getDomainObject(userId);

        MissionSystem missionSystem = MissionSystem.getInstance();
        if (Strings.isNullOrEmpty(param)) {
            Person person = user.getPerson();
            final Collection<Accountability> workingPlaceAccountabilities =
                    person.getParentAccountabilities(missionSystem.getAccountabilityTypesRequireingAuthorization());
            final SortedSet<Accountability> set = new TreeSet<>(this::compareAccoutabilities);
            set.addAll(workingPlaceAccountabilities);
            model.addAttribute("workerAccountabilities", set);
            result = generateWorkingsJson(result, workingPlaceAccountabilities);
            return result.toString();
        } else {
            final Accountability accountability = FenixFramework.getDomainObject(param);
            final Set<AccountabilityType> accountabilityTypes =
                    MissionSystem.getInstance().getAccountabilityTypesForAuthorization(accountability.getAccountabilityType());
            final Collection<AuthorizationChain> participantAuthorizationChain =
                    ParticipantAuthorizationChain.getParticipantAuthorizationChains(accountabilityTypes, accountability);
            model.addAttribute("participantAuthorizationChain", participantAuthorizationChain);
            result = generateAuthorChainJson(result, participantAuthorizationChain, missionSystem);
            return result.toString();
        }
    }

    private int compareAccoutabilities(final Accountability a1, final Accountability a2) {
        final LocalDate ld1 = a1.getBeginDate();
        final LocalDate ld2 = a2.getBeginDate();
        return ld1 == null && ld2 == null ? a1.getExternalId().compareTo(a2.getExternalId()) : ld1 == null ? -1 : ld2 == null ? 1 : -ld1
                .compareTo(ld2);
    }

    private JsonArray generateWorkingsJson(JsonArray result, final Collection<Accountability> worksPlace) {

        for (final Accountability ac : worksPlace) {

            JsonObject o = new JsonObject();
            o.addProperty("id", ac.getExternalId());
            o.addProperty("externalId", ac.getParent().getExternalId());
            o.addProperty("presentationName", ac.getParent().getPresentationName());
            o.addProperty("content", ac.getAccountabilityType().getName().getContent());
            o.addProperty("beginDate", ac.getBeginDate().toString());
            o.addProperty("endDate", ac.getEndDate() == null ? "" : ac.getEndDate().toString());
            o.add("details", new JsonArray());
            result.add(o);
        }

        return result;
    }

    private JsonArray generateAuthorChainJson(JsonArray result,
            final Collection<AuthorizationChain> participantAuthorizationChain, final MissionSystem missionSystem) {
        int order = 0;

        for (final AuthorizationChain participantAuthorization : participantAuthorizationChain) {

            for (AuthorizationChain authorizationChain = participantAuthorization; authorizationChain != null; authorizationChain =
                    authorizationChain.getNext()) {

                JsonObject o = new JsonObject();
                final Unit unit = authorizationChain.getUnit();
                final Collection<Accountability> authorities =
                        unit.getChildrenAccountabilities(new LocalDate(), new LocalDate(), missionSystem
                                .getAccountabilityTypesThatAuthorize().toArray(new AccountabilityType[0]));

                o.addProperty("size", String.valueOf(authorities.size()));
                o.addProperty("order", String.valueOf(++order));
                o.addProperty("unitId", unit.getExternalId());
                o.addProperty("unitName", unit.getPresentationName());
                JsonArray aa = new JsonArray();
                aa = getPersons(aa, authorities);
                o.add("persons", aa);

                result.add(o);

            }
        }

        return result;
    }

    private JsonArray getPersons(JsonArray aa, final Collection<Accountability> authorities) {

        for (final Accountability a : authorities) {
            JsonObject p = new JsonObject();
            p.addProperty("personId", a.getChild().getExternalId());
            p.addProperty("personName", a.getChild().getPresentationName());
            p.addProperty("type", a.getAccountabilityType().getName().getContent());
            aa.add(p);
        }
        return aa;
    }

    @RequestMapping(value = "/addMissionResponsability", method = RequestMethod.GET)
    public String addDelegationsForAuthorization(@RequestParam(required = true) String id,
            @RequestParam(required = true) String userId, @RequestParam(required = true) String unitId, @RequestParam(
                    required = true) String authorityType, @RequestParam(required = true) String beginDate, final Model model)
            throws Exception {

        final User user = userId != null || !userId.isEmpty() ? FenixFramework.getDomainObject(userId) : null;
        final Unit unit = unitId != null || !unitId.isEmpty() ? FenixFramework.getDomainObject(unitId) : null;
        final AccountabilityType accountabilityType =
                (authorityType != null || !authorityType.isEmpty() ? FenixFramework.getDomainObject(authorityType) : null);
        final DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
        final LocalDate local = formatter.parseLocalDate(beginDate);
        model.addAttribute("notActive", false);
        model.addAttribute("invalidData", false);
        if (unit != null && user != null && accountabilityType != null && local != null) {
            unit.addChild(user.getPerson(), accountabilityType, local, null, null);
            DomainObject o = FenixFramework.getDomainObject(id);

            if (o instanceof Unit) {

                model.addAttribute("notAutorize", false);
                return showUnit(unit, model);
            }
            if (o instanceof User) {
                model.addAttribute("linha", "");
                model.addAttribute("er", true);
                return showPerson(user, model);
            }
        } else {
            model.addAttribute("invalidData", true);
        }
        return "expenditure-tracking/prepareRelationshipType/" + id;
    }

    @RequestMapping(value = "/removeMissionResponsability", method = RequestMethod.GET)
    public String removeMissionResponsability(@RequestParam(required = true) String accountId,
            @RequestParam(required = true) String partyId, final Model model) throws Exception {
        final DomainObject o = FenixFramework.getDomainObject(accountId);
        final User u = FenixFramework.getDomainObject(partyId);

        model.addAttribute("er", false);
        if (ExpenditureTrackingSystem.getInstance().getAcquisitionsUnitManagerGroup().isMember(Authenticate.getUser())) {
            if (o instanceof Accountability) {
                final Accountability ac = (Accountability) o;
                try {
                    ac.setEndDate(new LocalDate());
                } catch (Exception e) {
                    model.addAttribute("linha", accountId);
                    model.addAttribute("er", true);
                }
            }
        }

        return showPerson(u, model);
    }

    @RequestMapping(value = "/removeSubUnit", method = RequestMethod.GET)
    public String removeSubUnit(@RequestParam(required = true) String accountId, @RequestParam(required = true) String partyId,
            final Model model) throws Exception {
        final DomainObject o = FenixFramework.getDomainObject(accountId);
        final Unit u = FenixFramework.getDomainObject(partyId);
        model.addAttribute("notActive", false);
        model.addAttribute("notAutorize", false);

        if (ExpenditureTrackingSystem.getInstance().getAcquisitionsUnitManagerGroup().isMember(Authenticate.getUser())) {
            if (o instanceof Accountability) {
                final Accountability ac = (Accountability) o;
                try {
                    ac.setEndDate(new LocalDate());
                } catch (Exception e) {

                }
            }
        }

        return showUnit(u, model);
    }

    @SuppressWarnings("null")
    @RequestMapping(value = "/addSubUnit", method = RequestMethod.GET)
    public String addSubUnit(@RequestParam(required = true) String id, @RequestParam(required = true) String unitId,
            @RequestParam(required = true) String type, @RequestParam(required = true) String beginDate, final Model model)
            throws Exception {

        final Unit parent = FenixFramework.getDomainObject(id);

        final Unit unit = unitId != null || !unitId.isEmpty() ? FenixFramework.getDomainObject(unitId) : null;
        final AccountabilityType accountabilityType =
                (type != null || !type.isEmpty() ? FenixFramework.getDomainObject(type) : null);
        final DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
        final LocalDate local = formatter.parseLocalDate(beginDate);
        model.addAttribute("invalidData", false);
        model.addAttribute("notAutorize", false);
        model.addAttribute("notActive", false);

        if (unit != null && parent != null && local != null && accountabilityType != null) {
            try {
                parent.addChild(unit, accountabilityType, local, null, null);
            } catch (Exception e) {

                model.addAttribute("addError", e.getLocalizedMessage());
                return prepareAddSubUnit(id, model);
            }
        }
        return showUnit(parent, model);
    }
}
