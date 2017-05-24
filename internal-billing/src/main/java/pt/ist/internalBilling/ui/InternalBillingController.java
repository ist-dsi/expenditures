package pt.ist.internalBilling.ui;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.UserProfile;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixframework.DomainObject;
import pt.ist.internalBilling.domain.Beneficiary;
import pt.ist.internalBilling.domain.Billable;
import pt.ist.internalBilling.domain.BillableLog;
import pt.ist.internalBilling.domain.BillableService;
import pt.ist.internalBilling.domain.BillableStatus;
import pt.ist.internalBilling.domain.BillableTransaction;
import pt.ist.internalBilling.domain.InternalBillingService;
import pt.ist.internalBilling.domain.PrintService;
import pt.ist.internalBilling.domain.UnitBeneficiary;
import pt.ist.internalBilling.domain.UserBeneficiary;
import pt.ist.internalBilling.util.Utils;

@SpringApplication(group = "logged", path = "internalBilling", title = "title.internalBilling",
        hint = "internal-billing")
@SpringFunctionality(app = InternalBillingController.class, title = "title.internalBilling")
@RequestMapping("/internalBilling")
public class InternalBillingController {

    private final MessageSource messageSource;

    @Autowired
    public InternalBillingController(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(final Model model) {
        final User user = Authenticate.getUser();

        final JsonArray pendingAuthorization = InternalBillingService.billablesPendingAuthorization(user)
                .map(b -> Utils.toJson(this::billable, b))
                .collect(Utils.toJsonArray());
        model.addAttribute("pendingAuthorization", pendingAuthorization);

        final JsonArray myServices = user.getUserBeneficiary().getBillableSet().stream()
                .map(b -> Utils.toJson(this::billable, b))
                .collect(Utils.toJsonArray());
        model.addAttribute("myServices", myServices);

        final JsonArray myUnits = user.getExpenditurePerson().getAuthorizationsSet().stream()
                .filter(a -> a.isValid())
                .map(a -> Utils.toJson(this::unitWithStatusCount, a.getUnit()))
                .collect(Utils.toJsonArray());
        model.addAttribute("myUnits", myUnits);

        return "internalBilling/home";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(final Model model, @RequestParam DomainObject unitOrUser) {
        final String pathPart = unitOrUser instanceof User ? "user" : "unit";
        return unitOrUser == null ? home(model) : "redirect:/internalBilling/" + pathPart + "/" + unitOrUser.getExternalId();
    }

    @RequestMapping(value = "/user/{user}", method = RequestMethod.GET)
    public String user(final Model model, @PathVariable final User user) {
        model.addAttribute("user", Utils.toJson(this::user, user));

        final JsonArray authorizations = user.getExpenditurePerson().getAuthorizationsSet().stream()
                .filter(a -> a.isValid())
                .map(a -> Utils.toJson(this::authorization, a))
                .collect(Utils.toJsonArray());
        model.addAttribute("authorizations", authorizations);

        final JsonArray observers = user.getExpenditurePerson().getObservableUnitsSet().stream()
                .map(u -> Utils.toJson(this::unit, u))
                .collect(Utils.toJsonArray());
        model.addAttribute("observers", observers);

        return "internalBilling/user";
    }

    @RequestMapping(value = "/user/{user}/services", method = RequestMethod.GET)
    public String userServices(final Model model, @PathVariable final User user) {
        model.addAttribute("user", Utils.toJson(this::user, user));

        final JsonArray services = InternalBillingService.getInstance().getBillableServiceSet().stream()
                .map(s -> service(user, s))
                .collect(Utils.toJsonArray());
            model.addAttribute("services", services);

        return "internalBilling/userServices";
    }

    @RequestMapping(value = "/user/{user}/logs", method = RequestMethod.GET)
    public String userLogs(final Model model, @PathVariable final User user) {
        model.addAttribute("user", Utils.toJson(this::user, user));

        final Stream<BillableLog> userLogs = user.getBillableLogSet().stream();
        final UserBeneficiary userBeneficiary = user.getUserBeneficiary();
        final Stream<BillableLog> beneficiaryLogs = userBeneficiary == null ? Stream.empty() :
                userBeneficiary.getBillableSet().stream()
                .flatMap(b -> b.getBillableLogSet().stream());

        final User currentUser = Authenticate.getUser();

        final JsonArray logs = Stream.concat(userLogs, beneficiaryLogs)
            .distinct()
            .filter(l -> isAllowedToView(currentUser, l))
            .sorted(BillableLog.COMPARATOR_BY_WHEN.reversed())
            .map(l -> Utils.toJson(this::log, l))
            .collect(Utils.toJsonArray());
        model.addAttribute("logs", logs);
        return "internalBilling/userLogs";
    }

    // TODO : unify method with same the corresponding unit method
    @RequestMapping(value = "/user/{user}/reports/byDay", method = RequestMethod.GET)
    public String unitReportsByDay(final Model model, @PathVariable final User user,
            @RequestParam final int year, @RequestParam final int month) {
        model.addAttribute("user", Utils.toJson(this::user, user));

        final User currentUser = Authenticate.getUser();

        final Map<LocalDate, Money> map = new TreeMap<LocalDate, Money>();
        user.getBillableTransactionSet().stream()
            .filter(tx -> match(year, month, tx.getTxDate()))
            .filter(tx -> isAllowedToView(currentUser, tx))
            .forEach(tx -> {
                final LocalDate txDate = tx.getTxDate().toLocalDate();
                final Money value = tx.getValue();
                final Money currentValue = map.get(txDate);
                final Money newValue = currentValue == null ? value : currentValue.add(value);
                map.put(txDate, newValue);
            });
        final JsonArray dayValuePairs = map.entrySet().stream()
                .map(e -> Utils.toJson(this::dayAndValue, e))
                .collect(Utils.toJsonArray());
        model.addAttribute("dayValuePairs", dayValuePairs);

        return "internalBilling/userReportsByDay";
    }

    @RequestMapping(value = "/user/{user}/reports/all", method = RequestMethod.GET)
    public String allReports(final Model model, @PathVariable final User user) {
        model.addAttribute("user", Utils.toJson(this::user, user));

        final User currentUser = Authenticate.getUser();
        final JsonArray transactions = user.getBillableTransactionSet().stream()
            .filter(tx -> isAllowedToView(currentUser, tx))
            .sorted(BillableTransaction.COMPARATOR_BY_DATE.reversed())
            .map(t -> Utils.toJson(this::transaction, t))
            .collect(Utils.toJsonArray());
        model.addAttribute("transactions", transactions);

        return "internalBilling/userReportsAll";
    }

    @RequestMapping(value = "/unit/{unit}/services", method = RequestMethod.GET)
    public String unitServices(final Model model, @PathVariable final Unit unit) {
        model.addAttribute("unit", Utils.toJson(this::unit, unit));

        final JsonArray services = InternalBillingService.getInstance().getBillableServiceSet().stream()
            .map(s -> service(unit, s))
            .collect(Utils.toJsonArray());
        model.addAttribute("services", services);

        return "internalBilling/unitServices";
    }

    @RequestMapping(value = "/unit/{unit}/reports", method = RequestMethod.GET)
    public String unitReports(final Model model, @PathVariable final Unit unit) {
        model.addAttribute("unit", Utils.toJson(this::unit, unit));
        return "internalBilling/unitReports";
    }

    @RequestMapping(value = "/unit/{unit}/reports/byDay", method = RequestMethod.GET)
    public String unitReportsByDay(final Model model, @PathVariable final Unit unit,
            @RequestParam final int year, @RequestParam final int month) {
        model.addAttribute("unit", Utils.toJson(this::unit, unit));

        final User currentUser = Authenticate.getUser();
        final Map<LocalDate, Money> map = new TreeMap<LocalDate, Money>();
        unit.getBillableSet().stream()
            .flatMap(b -> b.getBillableTransactionSet().stream())
            .filter(tx -> match(year, month, tx.getTxDate()))
            .filter(tx -> isAllowedToView(currentUser, tx))
            .forEach(tx -> {
                final LocalDate txDate = tx.getTxDate().toLocalDate();
                final Money value = tx.getValue();
                final Money currentValue = map.get(txDate);
                final Money newValue = currentValue == null ? value : currentValue.add(value);
                map.put(txDate, newValue);
            });
        final JsonArray dayValuePairs = map.entrySet().stream()
                .map(e -> Utils.toJson(this::dayAndValue, e))
                .collect(Utils.toJsonArray());
        model.addAttribute("dayValuePairs", dayValuePairs);

        return "internalBilling/unitReportsByDay";
    }

    private boolean match(final int year, final int month, final DateTime txDate) {
        return txDate.getYear() == year && txDate.getMonthOfYear() == month;
    }

    @RequestMapping(value = "/unit/{unit}/logs", method = RequestMethod.GET)
    public String unitLogs(final Model model, @PathVariable final Unit unit) {
        model.addAttribute("unit", Utils.toJson(this::unit, unit));
        final User currentUser = Authenticate.getUser();
        final JsonArray logs = unit.getBillableSet().stream()
            .flatMap(b -> b.getBillableLogSet().stream())
            .filter(l -> isAllowedToView(currentUser, l))
            .sorted(BillableLog.COMPARATOR_BY_WHEN.reversed())
            .map(l -> Utils.toJson(this::log, l))
            .collect(Utils.toJsonArray());
        model.addAttribute("logs", logs);
        return "internalBilling/unitLogs";
    }

    @RequestMapping(value = "/unit/{unit}", method = RequestMethod.GET)
    public String unit(final Model model, @PathVariable final Unit unit) {
        model.addAttribute("unit", Utils.toJson(this::unitWithFamily, unit));

        final JsonArray authorizations = unit.getAuthorizationsSet().stream()
                .filter(a -> a.isValid())
                .map(a -> Utils.toJson(this::authorization, a))
                .collect(Utils.toJsonArray());
        model.addAttribute("authorizations", authorizations);

        final JsonArray observers = unit.getObserversSet().stream()
                .map(p -> Utils.toJson(this::user, p.getUser()))
                .collect(Utils.toJsonArray());
        model.addAttribute("observers", observers);

        return "internalBilling/unit";
    }

    @RequestMapping(value = "/unit/{unit}/transactions/{yearMonth}", method = RequestMethod.GET)
    public String unit(final Model model, @PathVariable final Unit unit, @PathVariable final String yearMonth) {
        model.addAttribute("unit", unit);
        final int year = Integer.parseInt(yearMonth.substring(0,  4));
        final int month = Integer.parseInt(yearMonth.substring(5,  7));
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("transactions", unit.getBillableSet().stream()
                .flatMap(b -> b.getBillableTransactionSet().stream())
                .filter(tx -> tx.getTxDate().getYear() == year && tx.getTxDate().getMonthOfYear() == month)
                .sorted(BillableTransaction.COMPARATOR_BY_DATE.reversed())
                .collect(Collectors.toList()));
        return "internalBilling/unitTransactions";
    }

    @RequestMapping(value = "/billable/{billable}/authorize", method = RequestMethod.POST)
    public String authorize(final Model model, @PathVariable Billable billable) {
        if (billable.getUnit().isCurrentUserResponsibleForUnit()) {
            billable.authorize();
        }
        return "redirect:/internalBilling/unit/" + billable.getUnit().getExternalId();
    }

    @RequestMapping(value = "/billable/{billable}/revoke", method = RequestMethod.POST)
    public String revoke(final Model model, @PathVariable Billable billable) {
        if (billable.getUnit().isCurrentUserResponsibleForUnit()) {
            billable.revoke();
        }
        return "redirect:/internalBilling/unit/" + billable.getUnit().getExternalId();
    }

    @RequestMapping(value = "/unit/{unit}/authorizeAll", method = RequestMethod.POST)
    public String authorize(final Model model, @PathVariable final Unit unit, @RequestParam Billable[] billable) {
        for (final Billable b : billable) {
            if (b.getUnit().isCurrentUserResponsibleForUnit()) {
                b.authorize();
            }
        }
        return "redirect:/internalBilling/unit/" + unit.getExternalId();
    }

    private void unit(final JsonObject result, final Unit unit) {
        result.addProperty("id", unit.getExternalId());
        result.addProperty("shortName", unit.getUnit().getAcronym());
        result.addProperty("name", unit.getUnit().getPresentationName());
        result.addProperty("relativePath", "/internalBilling/unit/" + unit.getExternalId());
        result.addProperty("isCurrentUserResponsibleForUnit", unit.isCurrentUserResponsibleForUnit());
        final User user = Authenticate.getUser();
        result.addProperty("isUnitObserver", unit.isUnitObserver(user));
    }

    private void unitWithFamily(final JsonObject result, final Unit unit) {
        unit(result, unit);
        final Unit parentUnit = unit.getParentUnit();
        if (parentUnit != null) {
            result.add("parent", Utils.toJson(this::unit, parentUnit));
        }
        final JsonArray subunits = unit.getSubUnitsSet().stream()
                .map(b -> Utils.toJson(this::unit, b))
                .collect(Utils.toJsonArray());
        result.add("subunits", subunits);
    }

    private void unitWithStatusCount(final JsonObject result, final Unit unit) {
        unit(result, unit);
        final int[] counts = new int[BillableStatus.values().length];
        unit.getBillableSet().forEach(b -> counts[b.getBillableStatus().ordinal()]++);
        result.addProperty("pendingAuthorizationCount", counts[BillableStatus.PENDING_AUTHORIZATION.ordinal()]);
        result.addProperty("activeCount", counts[BillableStatus.AUTHORIZED.ordinal()]);
        result.addProperty("revokedCount", counts[BillableStatus.REVOKED.ordinal()]);
    }

    private void beneficiary(final JsonObject result, final Beneficiary beneficiary) {
        final String type = beneficiary.getClass().getName();
        result.addProperty("type", type);
        final String name;
        if (beneficiary instanceof UserBeneficiary) {
            final UserBeneficiary userBeneficiary = (UserBeneficiary) beneficiary;
            final User user = userBeneficiary.getUser();
            final UserProfile profile = user.getProfile();
            name = profile.getDisplayName();
            result.addProperty("avatarUrl", profile.getAvatarUrl());
            result.addProperty("relativePath", "/internalBilling/user/" + user.getExternalId());
        } else if (beneficiary instanceof UnitBeneficiary) {
            final UnitBeneficiary unitBeneficiary = (UnitBeneficiary) beneficiary;
            name = unitBeneficiary.getUnit().getPresentationName();
        } else {
            throw new Error("unknown.beneficiary.type: " + type);
        }
        result.addProperty("name", name);
    }

    private void billable(final JsonObject result, final Billable b) {
        final BillableService service = b.getBillableService();
        final String serviceClass = service.getClass().getName();
        result.addProperty("serviceClass", serviceClass);
        result.addProperty("serviceClassDescription", messageSource.getMessage("label." + serviceClass, null, I18N.getLocale()));
        result.add("unit", Utils.toJson(this::unit, b.getUnit()));
        result.add("beneficiary", Utils.toJson(this::beneficiary, b.getBeneficiary()));
        final String billableStatus = b.getBillableStatus().name();
        final String serviceStatus = b.getServiceStatus().name();
        result.addProperty("billableStatus", billableStatus);
        result.addProperty("billableStatusDescription", messageSource.getMessage("label.internalBilling.billableService.status." + billableStatus, null, I18N.getLocale()));
        result.addProperty("serviceStatus", serviceStatus);
        result.addProperty("serviceStatusDescription", messageSource.getMessage("label.internalBilling.billableService.status." + serviceStatus, null, I18N.getLocale()));
    }

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private void authorization(final JsonObject result, final Authorization authorization) {
        final Person person = authorization.getPerson();
        final User user = person.getUser();
        result.add("user", Utils.toJson(this::user, user));

        final Unit unit = authorization.getUnit();
        result.add("unit", Utils.toJson(this::unit, unit));

        final LocalDate startDate = authorization.getStartDate();
        result.addProperty("startDate", startDate == null ? null : startDate.toString(DATE_FORMAT));
        final LocalDate endDate = authorization.getEndDate();
        result.addProperty("endDate", endDate == null ? null : endDate.toString(DATE_FORMAT));
        final Money maxAmount = authorization.getMaxAmount();
        result.addProperty("maxAmount", maxAmount.toFormatString());
    }

    private void user(final JsonObject result, final User user) {
        final UserProfile profile = user.getProfile();
        result.addProperty("username", profile.getUser().getUsername());
        result.addProperty("name", profile.getDisplayName());
        result.addProperty("avatarUrl", profile.getAvatarUrl());
        result.addProperty("relativePath", "/internalBilling/user/" + user.getExternalId());
    }

    private JsonObject service(final User user, final BillableService service) {
        final JsonObject result = new JsonObject();
        result.addProperty("id", service.getExternalId());
        final String serviceClass = service.getClass().getName();
        result.addProperty("serviceClass", serviceClass);
        result.addProperty("serviceClassDescription", messageSource.getMessage("label." + serviceClass, null, I18N.getLocale()));
        result.addProperty("title", service.getTitle());
        final User currentUser = Authenticate.getUser();
        final UserBeneficiary userBeneficiary = user.getUserBeneficiary();
        final JsonArray billables = userBeneficiary == null ? new JsonArray() :
            userBeneficiary.getBillableSet().stream()
                .filter(b -> b.getBillableService() == service)
                .filter(b -> isAllowedToView(currentUser, b))
                .map(b -> Utils.toJson(this::billableForUserAndService, b))
                .collect(Utils.toJsonArray());
        result.add("billables", billables);
        return result;
    }

    // TODO : merge this method and method from above
    private JsonObject service(final Unit unit, final BillableService service) {
        final JsonObject result = new JsonObject();
        result.addProperty("id", service.getExternalId());
        final String serviceClass = service.getClass().getName();
        result.addProperty("serviceClass", serviceClass);
        result.addProperty("serviceClassDescription", messageSource.getMessage("label." + serviceClass, null, I18N.getLocale()));
        result.addProperty("title", service.getTitle());
        final User currentUser = Authenticate.getUser();
        final JsonArray billables = unit.getBillableSet().stream()
                .filter(b -> b.getBillableService() == service)
                .filter(b -> isAllowedToView(currentUser, b))
                .map(b -> Utils.toJson(this::billableForUnitAndService, b))
                .collect(Utils.toJsonArray());
        result.add("billables", billables);
        return result;
    }

    private void billableForUnitAndService(final JsonObject result, final Billable b) {
        result.addProperty("id", b.getExternalId());
        result.add("beneficiary", Utils.toJson(this::beneficiary, b.getBeneficiary()));
        final String billableStatus = b.getBillableStatus().name();
        final String serviceStatus = b.getServiceStatus().name();
        result.addProperty("billableStatus", billableStatus);
        result.addProperty("billableStatusDescription", messageSource.getMessage("label.internalBilling.billableService.status." + billableStatus, null, I18N.getLocale()));
        result.addProperty("serviceStatus", serviceStatus);
        result.addProperty("serviceStatusDescription", messageSource.getMessage("label.internalBilling.billableService.status." + serviceStatus, null, I18N.getLocale()));

        final BillableService service = b.getBillableService();
        if (service instanceof PrintService) {
            final PrintService printService = (PrintService) service;
            final Money authorizedValue = printService.authorizedValueFor(b);
            result.addProperty("authorizedValue", authorizedValue.toFormatString());
        }
    }

    // TODO : unify with previous method
    private void billableForUserAndService(final JsonObject result, final Billable b) {
        result.addProperty("id", b.getExternalId());
        result.add("financer", Utils.toJson(this::unit, b.getUnit()));
        final String billableStatus = b.getBillableStatus().name();
        final String serviceStatus = b.getServiceStatus().name();
        result.addProperty("billableStatus", billableStatus);
        result.addProperty("billableStatusDescription", messageSource.getMessage("label.internalBilling.billableService.status." + billableStatus, null, I18N.getLocale()));
        result.addProperty("serviceStatus", serviceStatus);
        result.addProperty("serviceStatusDescription", messageSource.getMessage("label.internalBilling.billableService.status." + serviceStatus, null, I18N.getLocale()));

        final BillableService service = b.getBillableService();
        if (service instanceof PrintService) {
            final PrintService printService = (PrintService) service;
            final Money authorizedValue = printService.authorizedValueFor(b);
            result.addProperty("authorizedValue", authorizedValue.toFormatString());
        }
    }

    private void log(final JsonObject result, final BillableLog log) {
        result.addProperty("description", log.getDescription());
        result.add("user", Utils.toJson(this::user, log.getUser()));
        result.addProperty("when", log.getWhenInstant().toString("yyyy-MM-dd HH:mm:ss"));
    }

    private void dayAndValue(final JsonObject result, final Entry<LocalDate, Money> e) {
        result.addProperty("dayOfMonth", e.getKey().getDayOfMonth());
        result.addProperty("value", e.getValue().getValue());
   }

    private void transaction(final JsonObject result, final BillableTransaction t) {
        result.addProperty("txDate", t.getTxDate().toString("yyyy-MM-dd HH:mm:ss"));
        result.addProperty("value", t.getValue().toFormatString());
        result.addProperty("label", t.getLabel());
        result.addProperty("description", t.getDescription());
        final Billable billable = t.getBillable();
        if (billable != null) {
            result.add("unit", Utils.toJson(this::unit, billable.getUnit()));
        }
    }

    private boolean isAllowedToView(final User currentUser, final BillableLog log) {
        return log.getUser() == currentUser || isUserResponsibleOrObserverForBillable(currentUser, log) || isManager(currentUser);
    }

    private boolean isAllowedToView(final User currentUser, final Billable billable) {
        return isBillableForCurrentUser(currentUser, billable) || isUserResponsibleOrObserverForBillable(currentUser, billable) || isManager(currentUser);
    }

    private boolean isAllowedToView(final User currentUser, final BillableTransaction tx) {
        return tx.getUser() == currentUser || isUserResponsibleOrObserverForBillable(currentUser, tx) || isManager(currentUser);
    }

    private boolean isUserResponsibleOrObserverForBillable(final User currentUser, final Billable billable) {
        final Unit unit = billable.getUnit();
        final Person person = currentUser.getExpenditurePerson();
        return Unit.isResponsible(unit.getUnit(), person) || unit.isUnitObserver(currentUser);
    }

    private boolean isUserResponsibleOrObserverForBillable(final User currentUser, final BillableTransaction tx) {
        final Billable billable = tx.getBillable();
        return billable != null && isUserResponsibleOrObserverForBillable(currentUser, billable);
    }

    private boolean isUserResponsibleOrObserverForBillable(final User currentUser, final BillableLog log) {
        final Billable billable = log.getBillable();
        return billable != null && isAllowedToView(currentUser, billable);
    }

    private boolean isBillableForCurrentUser(final User currentUser, final Billable billable) {
        final Beneficiary beneficiary = billable.getBeneficiary();
        return beneficiary instanceof UserBeneficiary && ((UserBeneficiary) beneficiary).getUser() == currentUser;
    }

    private boolean isManager(final User currentUser) {
        return DynamicGroup.get("managers").isMember(currentUser);
    }

}
