package pt.ist.internalBilling.ui;

import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import module.finance.util.Money;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.StringNormalizer;
import org.fenixedu.commons.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.internalBilling.domain.BillableLog;
import pt.ist.internalBilling.domain.BillableService;
import pt.ist.internalBilling.domain.BillableService.BillableServiceRequest;
import pt.ist.internalBilling.domain.InternalBillingService;
import pt.ist.internalBilling.domain.PrintService.PrintServiceRequest;
import pt.ist.internalBilling.domain.UserBeneficiary;
import pt.ist.internalBilling.util.Utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SpringFunctionality(app = InternalBillingController.class, title = "title.internalBilling.billableServices")
@RequestMapping("/internalBilling/billableService")
public class BillableServiceController {

    private final MessageSource messageSource;

    @Autowired
    public BillableServiceController(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(final Model model) {
        final JsonArray billableServices =
                InternalBillingService.billableServiceStream().map(s -> Utils.toJson(this::billableService, s))
                        .collect(Utils.toJsonArray());
        model.addAttribute("billableServices", billableServices);
        return "internalBilling/billableServices";
    }

    private void billableService(final JsonObject jo, final BillableService s) {
        jo.addProperty("id", s.getExternalId());
        jo.addProperty("type", s.getClass().getName());
        jo.addProperty("title", s.getTitle());
        jo.addProperty("description", s.getDescription());
        jo.addProperty("type", messageSource.getMessage("label." + s.getClass().getName(), null, I18N.getLocale()));
    }

    @RequestMapping(value = "/createService", method = RequestMethod.GET)
    public String prepareCreateService(final Model model) {
        return "internalBilling/billableServiceCreate";
    }

    @RequestMapping(value = "/createService", method = RequestMethod.POST)
    public String createService(final Model model, @RequestParam final String title, @RequestParam final String description,
            @RequestParam final String type, @RequestParam(required = false) final String costPerBlackAndWhiteCopy,
            @RequestParam(required = false) final String costPerColourCopy) {
        if (ExpenditureTrackingSystem.isManager()) {
            BillableService.create(type, title, description, toMoney(costPerBlackAndWhiteCopy), toMoney(costPerColourCopy));
        }
        return "redirect:/internalBilling/billableService";
    }

    static Money toMoney(final String s) {
        if (s == null || s.isEmpty()) {
            return Money.ZERO;
        }
        final String v = s.matches("-?\\d+(\\,\\d+)?") ? s.replace(',', '.') : s;
        return new Money(v);
    }

    @RequestMapping(value = "/{billableService}/delete", method = RequestMethod.POST)
    public String delete(final Model model, @PathVariable final BillableService billableService) {
        if (ExpenditureTrackingSystem.isManager()) {
            billableService.delete();
        }
        return "redirect:/internalBilling/billableService";
    }

    @RequestMapping(value = "/{billableService}/edit", method = RequestMethod.GET)
    public String prepareEdit(final Model model, @PathVariable final BillableService billableService) {
        model.addAttribute("billableService", Utils.toJson(this::billableService, billableService));
        return "internalBilling/billableServiceEdit";
    }

    @RequestMapping(value = "/{billableService}/edit", method = RequestMethod.POST)
    public String edit(final Model model, @PathVariable final BillableService billableService, @RequestParam final String title,
            @RequestParam final String description) {
        if (ExpenditureTrackingSystem.isManager()) {
            billableService.edit(title, description);
        }
        return "redirect:/internalBilling/billableService";
    }

    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    public String subscribe(final Model model, @RequestParam final BillableService billableService,
            @RequestParam final Unit financer, @RequestParam final String beneficiaryConfig) {

        final JsonArray configArray = new JsonParser().parse(beneficiaryConfig).getAsJsonArray();
        final BillableServiceRequest[] requests = new BillableServiceRequest[configArray.size()];
        for (int i = 0; i < configArray.size(); i++) {
            final JsonObject jo = configArray.get(i).getAsJsonObject();

            final PrintServiceRequest request = new PrintServiceRequest();
            final User user = Utils.readDomainObject(jo, "beneficiaryId");
            request.beneficiary = UserBeneficiary.beneficiaryFor(user);
            request.financer = financer;
            request.maxValue = toMoney(jo.get("maxValue").getAsString());

            requests[i] = request;
        }
        billableService.request(requests);

        return "redirect:/internalBilling/unit/" + financer.getExternalId() + "/services";
    }

    @RequestMapping(value = "/subscribeService", method = RequestMethod.GET)
    public String prepareSubscribeService(final Model model, @RequestParam final BillableService billableService) {
        model.addAttribute("billableService", billableService);
        return "internalBilling/subscribeService";
    }

    @SkipCSRF
    @RequestMapping(value = "/availableParties", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public @ResponseBody String availableParties(@RequestParam(required = false, value = "term") String term, final Model model) {
        final JsonArray result = new JsonArray();
        final String trimmedValue = term.trim();
        final String[] input = StringNormalizer.normalize(trimmedValue).split(" ");

        if (Authenticate.isLogged()) {
            findUnits(result, input);
            findPeople(result, input);
        }

        return result.toString();
    }

    @SkipCSRF
    @RequestMapping(value = "/availableUnits", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public @ResponseBody String availableUnits(@RequestParam(required = false, value = "term") String term, final Model model) {
        final JsonArray result = new JsonArray();
        final String trimmedValue = term.trim();
        final String[] input = StringNormalizer.normalize(trimmedValue).split(" ");

        if (Authenticate.isLogged()) {
            findUnits(result, input);
        }

        return result.toString();
    }

    @SkipCSRF
    @RequestMapping(value = "/availableCostCenters", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public @ResponseBody String availableCostCenters(@RequestParam(required = false, value = "term") String term,
            final Model model) {
        final JsonArray result = new JsonArray();
        final String trimmedValue = term.trim();
        final String[] input = StringNormalizer.normalize(trimmedValue).split(" ");

        if (Authenticate.isLogged()) {
            findCostCenters(result, input);
        }

        return result.toString();
    }

    @SkipCSRF
    @RequestMapping(value = "/availablePeople", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public @ResponseBody String availablePeople(@RequestParam(required = false, value = "term") String term, final Model model) {
        final JsonArray result = new JsonArray();
        final String trimmedValue = term.trim();
        final String[] input = StringNormalizer.normalize(trimmedValue).split(" ");

        if (Authenticate.isLogged()) {
            findPeople(result, input);
        }

        return result.toString();
    }

    @RequestMapping(value = "/viewLogs", method = RequestMethod.GET)
    public String viewLogs(final Model model, @RequestParam final BillableService billableService, @RequestParam final Unit unit) {
        model.addAttribute("billableService", billableService);
        model.addAttribute("unit", unit);
        final Supplier<TreeSet<BillableLog>> supplier = () -> new TreeSet<BillableLog>(BillableLog.COMPARATOR_BY_WHEN.reversed());
        final Set<BillableLog> logs =
                unit.getBillableSet().stream().flatMap(b -> b.getBillableLogSet().stream())
                        .collect(Collectors.toCollection(supplier));
        model.addAttribute("logs", logs);
        return "internalBilling/viewLogs";
    }

    @RequestMapping(value = "/viewUserLogs", method = RequestMethod.GET)
    public String viewLogs(final Model model, @RequestParam final User user) {
        model.addAttribute("user", user);
        final Supplier<TreeSet<BillableLog>> supplier = () -> new TreeSet<BillableLog>(BillableLog.COMPARATOR_BY_WHEN.reversed());
        final Stream<BillableLog> userLogs = user.getBillableLogSet().stream();
        final Stream<BillableLog> billableLogs =
                user.getUserBeneficiary().getBillableSet().stream().flatMap(b -> b.getBillableLogSet().stream());
        final Set<BillableLog> logs = Stream.concat(userLogs, billableLogs).distinct().collect(Collectors.toCollection(supplier));
        model.addAttribute("logs", logs);
        return "internalBilling/viewUserLogs";
    }

    private void findUnits(JsonArray result, String[] input) {
        ExpenditureTrackingSystem.getInstance().getUnitsSet().forEach(u -> findUnits(result, input, u));
    }

    private void findCostCenters(JsonArray result, String[] input) {
        ExpenditureTrackingSystem.getInstance().getUnitsSet().stream().filter(u -> u instanceof CostCenter)
                .forEach(u -> findUnits(result, input, u));
    }

    private void findUnits(JsonArray result, String[] input, Unit unit) {
        if (hasMatch(input, StringNormalizer.normalize(unit.getPresentationName()).toLowerCase())) {
            final JsonObject o = new JsonObject();
            o.addProperty("id", unit.getExternalId());
            o.addProperty("name", unit.getPresentationName());
            result.add(o);
        }
    }

    private boolean hasMatch(final String[] input, final String unitNameParts) {
        for (final String namePart : input) {
            if (unitNameParts.indexOf(namePart) == -1) {
                return false;
            }
        }
        return true;
    }

    private void findPeople(JsonArray result, String[] input) {
        findPeople(input).forEach(u -> addPersonToJson(result, u));
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

    private void addPersonToJson(JsonArray result, User u) {
        final JsonObject o = new JsonObject();
        o.addProperty("id", u.getExternalId());
        o.addProperty("name", u.getProfile().getDisplayName() + " (" + u.getUsername() + ")");
        result.add(o);
    }

}
