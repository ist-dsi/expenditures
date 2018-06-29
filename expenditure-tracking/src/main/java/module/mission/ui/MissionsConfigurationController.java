package module.mission.ui;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import module.mission.domain.AccountabilityTypeQueue;
import module.mission.domain.MissionAuthorizationAccountabilityType;
import module.mission.domain.MissionSystem;
import module.organization.domain.AccountabilityType;
import module.workflow.domain.WorkflowQueue;
import module.workflow.domain.WorkflowSystem;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.util.RedirectToStrutsAction;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

@SpringApplication(group = "#managers", path = "missions-module", title = "missionsConfiguration.title", hint = "missions-module")
@SpringFunctionality(app = MissionsConfigurationController.class, title = "missionsConfiguration.title")
@RequestMapping("/missions/config")
public class MissionsConfigurationController {

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
    public String selectCountry(final Model model) throws Exception {
        return RedirectToStrutsAction.redirect("configureMissions", "selectCountry");
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
    public String addMissionAuthorizationAccountabilityType(final Model model) throws Exception {
        return RedirectToStrutsAction.redirect("configureMissions", "prepareAddMissionAuthorizationAccountabilityType");
    }

    @RequestMapping(value = "/deleteMissionAuthorizationAccountabilityType/{id}", method = RequestMethod.GET)
    public String deleteMissionAuthorizationAccountabilityType(
            @PathVariable("id") final String missionAuthorizationAccountabilityTypeOid, final Model model) throws Exception {
        final MissionAuthorizationAccountabilityType missionAuthorizationAccountabilityType =
                FenixFramework.getDomainObject(missionAuthorizationAccountabilityTypeOid);
        missionAuthorizationAccountabilityType.delete();

        return "redirect:/missions/config";
    }

    @RequestMapping(value = "/setAllowGrantOwnerMissionProcessNature", method = RequestMethod.POST)
    public String setAllowGrantOwnerMissionProcessNature(
            @RequestParam(name = "allowGrantOwnerEquivalence", required = false) String allowGrantOwnerEquivalence,
            final Model model) {
        setAllowGrantOwnerEquivalence(allowGrantOwnerEquivalence != null);

        return "redirect:/missions/config";
    }

    @Atomic(mode = TxMode.WRITE)
    private void setAllowGrantOwnerEquivalence(boolean allow) {
        MissionSystem.getInstance().setAllowGrantOwnerEquivalence(allow);
    }

    @RequestMapping(value = "/setUseWorkingPlaceAuthorizationChain", method = RequestMethod.POST)
    public String setUseWorkingPlaceAuthorizationChain(
            @RequestParam(name = "useWorkingPlaceAuthorizationChain", required = false) String useWorkingPlaceAuthorizationChain,
            final Model model) {
        setUseWorkingPlaceAuthorizationChain(useWorkingPlaceAuthorizationChain != null);

        return "redirect:/missions/config";
    }

    @Atomic(mode = TxMode.WRITE)
    private void setUseWorkingPlaceAuthorizationChain(boolean use) {
        MissionSystem.getInstance().setUseWorkingPlaceAuthorizationChain(use);
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
    public String setVerificationQueue(@RequestParam(name = "verificationQueueOid") String verificationQueueOid,
            final Model model) {
        final WorkflowQueue queue = FenixFramework.getDomainObject(verificationQueueOid);
        setVerificationQueue(queue);

        return "redirect:/missions/config";
    }

    @Atomic(mode = TxMode.WRITE)
    private void setVerificationQueue(WorkflowQueue queue) {
        MissionSystem.getInstance().setVerificationQueue(queue);
    }

    @RequestMapping(value = "/setEmploymentAccountabilityType", method = RequestMethod.POST)
    public String setEmploymentAccountabilityType(@RequestParam(name = "accountabilityTypeOid") String accountabilityTypeOid,
            final Model model) {
        final AccountabilityType type = FenixFramework.getDomainObject(accountabilityTypeOid);
        setEmploymentAccountabilityType(type);

        return "redirect:/missions/config";
    }

    @Atomic(mode = TxMode.WRITE)
    private void setEmploymentAccountabilityType(AccountabilityType type) {
        MissionSystem.getInstance().setEmploymentAccountabilityType(type);
    }

    @RequestMapping(value = "/addQueueForAccountabilityType", method = RequestMethod.GET)
    public String addQueueForAccountabilityType(final Model model) throws Exception {
        return RedirectToStrutsAction.redirect("configureMissions", "prepareAddQueueForAccountabilityType");
    }

    @RequestMapping(value = "/deleteAccountabilityTypeQueue/{id}", method = RequestMethod.GET)
    public String deleteAccountabilityTypeQueue(@PathVariable("id") final String accountabilityTypeQueueOid, final Model model)
            throws Exception {
        final AccountabilityTypeQueue typeQueue = FenixFramework.getDomainObject(accountabilityTypeQueueOid);
        typeQueue.delete();

        return "redirect:/missions/config";
    }

    @RequestMapping(value = "/showUnit/{id}", method = RequestMethod.GET)
    public String showUnit(@PathVariable("id") final String unitId, final Model model) throws Exception {
        return RedirectToStrutsAction.redirect("missionOrganization", "showUnitById", "unitId", unitId);
    }

    @RequestMapping(value = "/addUserWhoCanCancelMissions", method = RequestMethod.GET)
    public String addUserWhoCanCancelMissions(final Model model) throws Exception {
        return RedirectToStrutsAction.redirect("configureMissions", "prepareAddUserWhoCanCancelMissions");
    }

    @RequestMapping(value = "/removeUserWhoCanCancelMissions/{username}", method = RequestMethod.GET)
    public String removeUserWhoCanCancelMissions(@PathVariable("username") final String username, final Model model)
            throws Exception {
        final User user = User.findByUsername(username);
        MissionSystem.getInstance().removeUsersWhoCanCancelMission(user);

        return "redirect:/missions/config";
    }

    @RequestMapping(value = "/addVehicleAuthorizer", method = RequestMethod.GET)
    public String addVehicleAuthorizer(final Model model) throws Exception {
        return RedirectToStrutsAction.redirect("configureMissions", "prepareAddVehicleAuthorizer");
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
}