package pt.ist.internalBilling.api;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.fenixedu.bennu.InternalBillingConfiguration;
import org.fenixedu.bennu.InternalBillingConfiguration.ConfigurationProperties;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.UserProfile;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.internalBilling.BillingInformationHook;
import pt.ist.internalBilling.domain.Billable;
import pt.ist.internalBilling.domain.BillableService;
import pt.ist.internalBilling.domain.BillableStatus;
import pt.ist.internalBilling.domain.CurrentBillableHistory;
import pt.ist.internalBilling.domain.PrintService;
import pt.ist.internalBilling.domain.UserBeneficiary;
import pt.ist.internalBilling.util.Utils;

@Path("/internalBilling/v1")
public class InternalBillingAPIv1 {

    public final static String JSON_UTF8 = "application/json; charset=utf-8";

    @GET
    @Produces(JSON_UTF8)
    @Path("print/user/{username}/info")
    public String userInfo(final @PathParam("username") String username, final @QueryParam("token") String token) {
        checkAppCredentials(token);
        final User user = User.findByUsername(username);
        return toJson(user).toString();
    }

    @POST
    @Produces(JSON_UTF8)
    @Path("print/user/{username}/setUnit/{unitId}")
    public String setUnit(final @PathParam("username") String username, final @PathParam("unitId") String unitId, final @QueryParam("token") String token) {
        checkAppCredentials(token);
        final User user = User.findByUsername(username);
        if (user != null) {
            try { 
                Authenticate.mock(user, "Print System Auth");
                final UserBeneficiary beneficiary = user.getUserBeneficiary();
                if (beneficiary != null) {
                    beneficiary.getBillableSet().stream()
                        .filter(b -> b.getBillableStatus() == BillableStatus.AUTHORIZED)
                        .filter(b -> b.getUnit().getExternalId().equals(unitId))
                        .filter(b -> b.getBillableService() instanceof PrintService)
                        .peek(b -> BillingInformationHook.HOOKS.forEach(h -> h.signalUnitChange(user, b.getUnit())))
                        .forEach(b -> b.setUserFromCurrentBillable(user)); // only 1 o 0 ... but this will do the job
                        ;
                }
            } finally {
                Authenticate.unmock();
            }
        }
        return toJson(user).toString();
    }

    private JsonObject toJson(final User user) {
        final JsonObject jo = new JsonObject();
        if (user != null) {
            final UserProfile profile = user.getProfile();
            jo.addProperty("username", user.getUsername());
            jo.addProperty("avatarUrl", profile.getAvatarUrl());
            jo.addProperty("name", profile.getDisplayName());

            final JsonObject currentBillingUnit = toJson(currentBillingUnitFor(user));
            jo.add("currentBillingUnit", currentBillingUnit);

            final JsonArray billingUnits = billingUnitsFor(user);
            jo.add("billingUnits", billingUnits);

            BillingInformationHook.HOOKS.forEach(h -> h.addInfoFor(jo, user));
        }
        return jo;
    }

    private JsonArray billingUnitsFor(final User user) {
        final UserBeneficiary beneficiary = user == null ? null : user.getUserBeneficiary();
        return beneficiary == null ? new JsonArray() : beneficiary.getBillableSet().stream()
                .filter(b -> b.getBillableStatus() == BillableStatus.AUTHORIZED)
                .filter(b -> b.getBillableService() instanceof PrintService)
                .map(b -> toJson(b))
                .collect(Utils.toJsonArray());
    }

    private Billable currentBillingUnitFor(final User user) {
        final CurrentBillableHistory currentBillableHistory = user.getCurrentBillableHistory();
        if (currentBillableHistory != null) {
            final Billable billable = currentBillableHistory.getBillable();
            if (billable != null) {
                final BillableStatus status = billable.getBillableStatus();
                if (status == BillableStatus.AUTHORIZED) {
                    final BillableService service = billable.getBillableService();
                    if (service instanceof PrintService) {
                        return billable;
                    }
                }
            }
        }
        return null;
    }

    private JsonObject toJson(final Billable billable) {
        if (billable != null) {
            final Unit unit = billable.getUnit();
            final PrintService service = (PrintService) billable.getBillableService();

            final JsonObject jo = toJson(unit);

            final Money maxValue = service.authorizedValueFor(billable);
            jo.addProperty("maxValue", maxValue.exportAsString());

            final DateTime dt = new DateTime();
            final Money value = billable.getBillableTransactionSet().stream()
                .filter(tx -> tx.getTxDate().getYear() == dt.getYear() && tx.getTxDate().getMonthOfYear() == dt.getMonthOfYear())
                .map(tx -> tx.getValue())
                .reduce(Money.ZERO, Money::add);
            jo.addProperty("consumptionForCurrentMonth", value.exportAsString());

            BillingInformationHook.HOOKS.forEach(h -> h.addInfoFor(jo, billable));

            return jo;
        }
        return null;
    }

    private JsonObject toJson(final Unit unit) {
        final JsonObject jo = new JsonObject();
        if (unit != null) {
            jo.addProperty("id", unit.getExternalId());
            jo.addProperty("shortIdentifier", unit.getShortIdentifier());
            jo.addProperty("name", unit.getName());
            jo.addProperty("presentationName", unit.getPresentationName());
        }
        return jo;
    }

    private void checkAppCredentials(final String token) {
        final ConfigurationProperties confifg = InternalBillingConfiguration.getConfiguration();
        final String printAppToken = confifg.printAppToken();
        if (printAppToken == null || printAppToken.isEmpty() || !printAppToken.equals(token)) {
            JsonObject errorObject = new JsonObject();
            errorObject.addProperty("error", "not.authorized");
            errorObject.addProperty("description", "You are not authorized to access this endpoint. Check your credentials.");
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).entity(errorObject.toString()).build());
        }
    }

}
