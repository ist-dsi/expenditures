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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.internalBilling.domain.Billable;
import pt.ist.internalBilling.domain.BillableService;
import pt.ist.internalBilling.domain.BillableStatus;
import pt.ist.internalBilling.domain.CurrentBillableHistory;
import pt.ist.internalBilling.domain.PrintService;
import pt.ist.internalBilling.domain.UserBeneficiary;

@Path("/internalBilling/v1")
public class InternalBillingAPIv1 {

    public final static String JSON_UTF8 = "application/json; charset=utf-8";

    @GET
    @Produces(JSON_UTF8)
    @Path("print/user/{username}/listUnits")
    public String listUnits(final @PathParam("username") String username, final @QueryParam("token") String token) {
        checkAppCredentials(token);
        final JsonArray result = new JsonArray();
        final User user = User.findByUsername(username);
        if (user != null) {
            final UserBeneficiary beneficiary = user.getUserBeneficiary();
            if (beneficiary != null) {
                for (final Billable billable : beneficiary.getBillableSet()) {
                    final BillableStatus status = billable.getBillableStatus();
                    if (status == BillableStatus.AUTHORIZED) {
                        final BillableService service = billable.getBillableService();
                        if (service instanceof PrintService) {
                            final Unit unit = billable.getUnit();

                            final JsonObject configuration = toJson(billable, unit);
                            result.add(configuration);
                        }
                    }
                }
            }
        }
        return result.toString();
    }

    @GET
    @Produces(JSON_UTF8)
    @Path("print/user/{username}/currentBillingInformation")
    public String currentPrintBillingInformation(final @PathParam("username") String username, final @QueryParam("token") String token) {
        checkAppCredentials(token);
        final User user = User.findByUsername(username);
        return currentPrintBillingInformation(user);
    }

    public String currentPrintBillingInformation(final User user) {
        if (user != null) {
            final CurrentBillableHistory currentBillableHistory = user.getCurrentBillableHistory();
            if (currentBillableHistory != null) {
                final Billable billable = currentBillableHistory.getBillable();
                if (billable != null) {
                    final BillableStatus status = billable.getBillableStatus();
                    if (status == BillableStatus.AUTHORIZED) {
                        final BillableService service = billable.getBillableService();
                        if (service instanceof PrintService) {
                            final Unit unit = billable.getUnit();

                            final JsonObject configuration = toJson(billable, unit);
                            return configuration.toString();
                        }
                    }
                }
            }
        }
        return new JsonObject().toString();
    }

    @POST
    @Produces(JSON_UTF8)
    @Path("print/user/{username}/setUnit/{unitId}")
    public String setUnit(final @PathParam("username") String username, final @PathParam("unitId") String unitId, final @QueryParam("token") String token) {
        checkAppCredentials(token);
        final User user = User.findByUsername(username);
        if (user != null) {
            final UserBeneficiary beneficiary = user.getUserBeneficiary();
            if (beneficiary != null) {
                for (final Billable billable : beneficiary.getBillableSet()) {
                    final BillableStatus status = billable.getBillableStatus();
                    if (status == BillableStatus.AUTHORIZED) {
                        final Unit unit = billable.getUnit();
                        if (unit.getExternalId().equals(unitId)) {
                            final BillableService service = billable.getBillableService();
                            if (service instanceof PrintService) {
                                billable.setUserFromCurrentBillable(user);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return currentPrintBillingInformation(user);
    }

    private JsonObject toJson(final Billable billable, final Unit unit) {
        final JsonObject jo = billable.getConfigurationAsJson();
        jo.addProperty("id", unit.getExternalId());
        jo.addProperty("shortIdentifier", unit.getShortIdentifier());
        jo.addProperty("name", unit.getName());
        jo.addProperty("presentationName", unit.getPresentationName());
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
