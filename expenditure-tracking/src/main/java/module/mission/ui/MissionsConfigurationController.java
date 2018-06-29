package module.mission.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.StringNormalizer;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import module.geography.domain.Country;
import module.mission.domain.AccountabilityTypeQueue;
import module.mission.domain.MissionAuthorizationAccountabilityType;
import module.mission.domain.MissionSystem;
import module.mission.domain.util.AccountabilityTypeQueueBean;
import module.mission.domain.util.MissionAuthorizationAccountabilityTypeBean;
import module.organization.domain.AccountabilityType;
import module.workflow.domain.WorkflowQueue;
import module.workflow.domain.WorkflowSystem;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.util.RedirectToStrutsAction;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

@SpringApplication(group = "#managers", path = "missions-module", title = "missionsConfiguration.title", hint = "missions-module")
@SpringFunctionality(app = MissionsConfigurationController.class, title = "missionsConfiguration.title")
@RequestMapping("/missions/config")
public class MissionsConfigurationController {

    private static final long MAX_AUTOCOMPLETE_COUNTRY_COUNT = 5;

    @RequestMapping(method = RequestMethod.GET)
    public String config(final Model model) throws Exception {
        final List<WorkflowQueue> workflowQueues = WorkflowSystem.getInstance().getWorkflowQueuesSet().stream()
                .sorted(WorkflowQueue.COMPARATOR_BY_NAME).collect(Collectors.toList());
        final List<AccountabilityType> accountabilityTypes = Bennu.getInstance().getAccountabilityTypesSet().stream()
                .sorted(AccountabilityType.COMPARATORY_BY_NAME).collect(Collectors.toList());

        model.addAttribute("missionSystem", MissionSystem.getInstance());
        model.addAttribute("workflowQueues", workflowQueues);
        model.addAttribute("accountabilityTypes", accountabilityTypes);

        model.addAttribute("createSupplierUrl", ExpenditureTrackingSystem.getInstance().getCreateSupplierUrl());
        model.addAttribute("createSupplierLabel", ExpenditureTrackingSystem.getInstance().getCreateSupplierLabel());
        return "missions/config/config";
    }

    @RequestMapping(value = "/selectCountry", method = RequestMethod.GET)
    public String prepareSelectCountry(final Model model) throws Exception {
        return "missions/config/selectCountry";
    }

    @RequestMapping(value = "/selectCountry", method = RequestMethod.POST)
    public String selectCountry(@RequestParam(name = "countryCode") String countryCode, final Model model) throws Exception {
        final Country c = Country.findByIso3166alpha3Code(countryCode);
        selectCountry(c);
        return "redirect:/missions/config";
    }

    @Atomic(mode = TxMode.WRITE)
    private void selectCountry(final Country c) {
        MissionSystem.getInstance().setCountry(c);
    }

    @RequestMapping(value = "/viewModel/{id}", method = RequestMethod.GET)
    public String viewModel(@PathVariable("id") final String organizationalModelOid, final Model model) throws Exception {
        return RedirectToStrutsAction.redirect("organizationModel", "viewModel", "organizationalModelOid",
                organizationalModelOid);
    }

    @RequestMapping(value = "/selectOrganizationalModel", method = RequestMethod.GET)
    public String selectOrganizationalModel(final Model model) throws Exception {
        return RedirectToStrutsAction.redirect("configureMissions", "prepareSelectOrganizationalModel");
    }

    @RequestMapping(value = "/addMissionAuthorizationAccountabilityType", method = RequestMethod.GET)
    public String prepareAddMissionAuthorizationAccountabilityType(final Model model) throws Exception {
        final List<AccountabilityType> accountabilityTypes = MissionSystem.getInstance().getOrganizationalModel()
                .getAccountabilityTypesSet().stream().sorted(AccountabilityType.COMPARATORY_BY_NAME).collect(Collectors.toList());
        model.addAttribute("accountabilityTypes", accountabilityTypes);

        return "missions/config/addMissionAuthorizationAccountabilityType";
    }

    @RequestMapping(value = "/addMissionAuthorizationAccountabilityType", method = RequestMethod.POST)
    public String addMissionAuthorizationAccountabilityType(
            MissionAuthorizationAccountabilityTypeBean missionAuthorizationAccountabilityTypeBean, final Model model)
            throws Exception {
        missionAuthorizationAccountabilityTypeBean.createMissionAuthorizationAccountabilityType();
        return "redirect:/missions/config";
    }

    @RequestMapping(value = "/deleteMissionAuthorizationAccountabilityType/{id}", method = RequestMethod.GET)
    public String deleteMissionAuthorizationAccountabilityType(
            @PathVariable("id") final MissionAuthorizationAccountabilityType missionAuthorizationAccountabilityType,
            final Model model) throws Exception {
        missionAuthorizationAccountabilityType.delete();

        return "redirect:/missions/config";
    }

    @RequestMapping(value = "/setAllowGrantOwnerMissionProcessNature", method = RequestMethod.POST)
    public String setAllowGrantOwnerMissionProcessNature(
            @RequestParam(name = "allowGrantOwnerEquivalence", required = false) String allowGrantOwnerEquivalence,
            final Model model) {
        final MissionSystem missionSystem = MissionSystem.getInstance();
        final boolean allow = allowGrantOwnerEquivalence != null;
        if (missionSystem.getAllowGrantOwnerEquivalence() != allow) {
            missionSystem.toggleAllowGrantOwnerEquivalence();
        }

        return "redirect:/missions/config";
    }

    @RequestMapping(value = "/setUseWorkingPlaceAuthorizationChain", method = RequestMethod.POST)
    public String setUseWorkingPlaceAuthorizationChain(
            @RequestParam(name = "useWorkingPlaceAuthorizationChain", required = false) String useWorkingPlaceAuthorizationChain,
            final Model model) {
        final MissionSystem missionSystem = MissionSystem.getInstance();
        final boolean use = useWorkingPlaceAuthorizationChain != null;
        if (missionSystem.getUseWorkingPlaceAuthorizationChain() != use) {
            missionSystem.togleUseWorkingPlaceAuthorizationChain();
        }

        return "redirect:/missions/config";
    }

    @RequestMapping(value = "/createDailyPersonelExpenseTable", method = RequestMethod.GET)
    public String createDailyPersonelExpenseTable(final Model model) throws Exception {
        return RedirectToStrutsAction.redirect("configureMissions", "prepareCreateDailyPersonelExpenseTable");
    }

    @RequestMapping(value = "/viewDailyPersonelExpenseTable/{id}", method = RequestMethod.GET)
    public String viewDailyPersonelExpenseTable(@PathVariable("id") final String dailyPersonelExpenseTableOid, final Model model)
            throws Exception {
        return RedirectToStrutsAction.redirect("configureMissions", "viewDailyPersonelExpenseTable",
                "dailyPersonelExpenseTableOid", dailyPersonelExpenseTableOid);
    }

    @RequestMapping(value = "/setVerificationQueue", method = RequestMethod.POST)
    public String setVerificationQueue(@RequestParam(name = "verificationQueueOid") WorkflowQueue queue, final Model model) {
        setVerificationQueue(queue);

        return "redirect:/missions/config";
    }

    @Atomic(mode = TxMode.WRITE)
    private void setVerificationQueue(WorkflowQueue queue) {
        MissionSystem.getInstance().setVerificationQueue(queue);
    }

    @RequestMapping(value = "/setEmploymentAccountabilityType", method = RequestMethod.POST)
    public String setEmploymentAccountabilityType(@RequestParam(name = "accountabilityTypeOid") AccountabilityType type,
            final Model model) {
        setEmploymentAccountabilityType(type);

        return "redirect:/missions/config";
    }

    @Atomic(mode = TxMode.WRITE)
    private void setEmploymentAccountabilityType(AccountabilityType type) {
        MissionSystem.getInstance().setEmploymentAccountabilityType(type);
    }

    @RequestMapping(value = "/addQueueForAccountabilityType", method = RequestMethod.GET)
    public String prepareAddQueueForAccountabilityType(final Model model) throws Exception {
        final List<AccountabilityType> accountabilityTypes = MissionSystem.getInstance().getOrganizationalModel()
                .getAccountabilityTypesSet().stream().sorted(AccountabilityType.COMPARATORY_BY_NAME).collect(Collectors.toList());
        final List<WorkflowQueue> workflowQueues = WorkflowSystem.getInstance().getWorkflowQueuesSet().stream()
                .sorted(WorkflowQueue.COMPARATOR_BY_NAME).collect(Collectors.toList());

        model.addAttribute("accountabilityTypes", accountabilityTypes);
        model.addAttribute("workflowQueues", workflowQueues);

        return "missions/config/addQueueForAccountabilityType";
    }

    @RequestMapping(value = "/addQueueForAccountabilityType", method = RequestMethod.POST)
    public String addQueueForAccountabilityType(AccountabilityTypeQueueBean accountabilityTypeQueueBean, final Model model)
            throws Exception {
        accountabilityTypeQueueBean.create();

        return "redirect:/missions/config";
    }

    @RequestMapping(value = "/deleteAccountabilityTypeQueue/{id}", method = RequestMethod.GET)
    public String deleteAccountabilityTypeQueue(@PathVariable("id") final AccountabilityTypeQueue typeQueue, final Model model)
            throws Exception {
        typeQueue.delete();

        return "redirect:/missions/config";
    }

    @RequestMapping(value = "/showUnit/{id}", method = RequestMethod.GET)
    public String showUnit(@PathVariable("id") final String unitId, final Model model) throws Exception {
        return RedirectToStrutsAction.redirect("missionOrganization", "showUnitById", "unitId", unitId);
    }

    @RequestMapping(value = "/addUserWhoCanCancelMissions", method = RequestMethod.GET)
    public String prepareAddUserWhoCanCancelMissions(final Model model) throws Exception {

        return "missions/config/addUserWhoCanCancelMissions";
    }

    @RequestMapping(value = "/addUserWhoCanCancelMissions", method = RequestMethod.POST)
    public String addUserWhoCanCancelMissions(@RequestParam("username") String username, final Model model) throws Exception {
        final User user = User.findByUsername(username);
        if (user != null) {
            MissionSystem.getInstance().addUsersWhoCanCancelMission(user);
        }

        return "redirect:/missions/config";
    }

    @RequestMapping(value = "/removeUserWhoCanCancelMissions/{username}", method = RequestMethod.GET)
    public String removeUserWhoCanCancelMissions(@PathVariable("username") final String username, final Model model)
            throws Exception {
        final User user = User.findByUsername(username);
        if (user != null) {
            MissionSystem.getInstance().removeUsersWhoCanCancelMission(user);
        }

        return "redirect:/missions/config";
    }

    @RequestMapping(value = "/addVehicleAuthorizer", method = RequestMethod.GET)
    public String prepareAddVehicleAuthorizer(final Model model) throws Exception {

        return "missions/config/addVehicleAuthorizer";
    }

    @RequestMapping(value = "/addVehicleAuthorizer", method = RequestMethod.POST)
    public String addVehicleAuthorizer(@RequestParam("username") String username, final Model model) throws Exception {
        final User user = User.findByUsername(username);
        if (user != null) {
            MissionSystem.getInstance().addVehicleAuthorizers(user);
        }

        return "redirect:/missions/config";
    }

    @RequestMapping(value = "/removeVehicleAuthorizer/{username}", method = RequestMethod.GET)
    public String removeVehicleAuthorizer(@PathVariable("username") final String username, final Model model) throws Exception {
        final User user = User.findByUsername(username);
        MissionSystem.getInstance().removeVehicleAuthorizers(user);

        return "redirect:/missions/config";
    }

    @RequestMapping(value = "/addMandatorySupplier", method = RequestMethod.GET)
    public String prepareAddMandatorySupplier(final Model model) throws Exception {
        return "missions/config/addMandatorySupplier";
    }

    @RequestMapping(value = "/addMandatorySupplier", method = RequestMethod.POST)
    public String addMandatorySupplier(@RequestParam(name = "supplierNif") String supplierNif, final Model model)
            throws Exception {
        final Supplier supplier = Supplier.readSupplierByFiscalIdentificationCode(supplierNif);
        MissionSystem.getInstance().addMandatorySupplierService(supplier);

        return "redirect:/missions/config";
    }

    @RequestMapping(value = "/removeMandatorySupplier/{nif}", method = RequestMethod.GET)
    public String removeMandatorySupplier(@PathVariable("nif") final String supplierNif, final Model model) throws Exception {
        final Supplier supplier = Supplier.readSupplierByFiscalIdentificationCode(supplierNif);
        MissionSystem.getInstance().removeMandatorySupplierService(supplier);

        return "redirect:/missions/config";
    }

    @RequestMapping(value = "/setMandatorySupplierNotUsedErrorMessageArg", method = RequestMethod.POST)
    public String setMandatorySupplierNotUsedErrorMessageArg(
            @RequestParam(name = "mandatorySupplierNotUsedErrorMessageArg") String mandatorySupplierNotUsedErrorMessageArg,
            final Model model) {
        setMandatorySupplierNotUsedErrorMessageArg(mandatorySupplierNotUsedErrorMessageArg);

        return "redirect:/missions/config";
    }

    @Atomic(mode = TxMode.WRITE)
    private void setMandatorySupplierNotUsedErrorMessageArg(String arg) {
        MissionSystem.getInstance().setMandatorySupplierNotUsedErrorMessageArg(arg);
    }

    /*
     * AUTOCOMPLETE
     */
    @RequestMapping(value = "/country/json", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public @ResponseBody String country(@RequestParam(required = false, value = "term") String term, final Model model) {
        final JsonArray result = new JsonArray();
        try {
            final String trimmedValue = URLDecoder.decode(term, "UTF-8").trim();
            final String[] input = StringNormalizer.normalize(trimmedValue).split(" ");
            findCountries(result, input, term);
            return result.toString();
        } catch (final UnsupportedEncodingException e) {
            throw new Error(e);
        }
    }

    private void findCountries(JsonArray result, String[] input, String term) {
        Bennu.getInstance().getCountriesSet().stream().filter(country -> countryHasMatch(country, term, input))
                .sorted(Comparator.comparing(u -> u.getName())).limit(MAX_AUTOCOMPLETE_COUNTRY_COUNT)
                .forEach(u -> addToJson(result, u));
    }

    private boolean countryHasMatch(Country country, String term, final String[] input) {
        final String acronym = country.getAcronym();
        final String code2 = country.getIso3166alpha2Code();
        final String code3 = country.getIso3166alpha3Code();
        final String numCode = country.getIso3166numericCode().toString();

        if (acronym.startsWith(term) || code2.startsWith(term) || code3.startsWith(term) || numCode.startsWith(term)) {
            return true;
        } else {
            final String name = StringNormalizer.normalize(country.getName().getContent());
            for (final String namePart : input) {
                if (name.indexOf(namePart) == -1) {
                    return false;
                }
            }
            return true;
        }
    }

    private void addToJson(JsonArray result, Country c) {
        final JsonObject o = new JsonObject();

        //o.addProperty("id", s.getExternalId());
        o.addProperty("name", c.getName().getContent());
        o.addProperty("acronym", c.getAcronym());
        o.addProperty("code2", c.getIso3166alpha2Code());
        o.addProperty("code3", c.getIso3166alpha3Code());
        o.addProperty("numCode", c.getIso3166numericCode());

        result.add(o);
    }
}