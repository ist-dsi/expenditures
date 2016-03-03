package pt.ist.internalBilling.domain;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

public class PrintService extends PrintService_Base {

    PrintService(final String title, final String description) {
        setTitle(title);
        setDescription(description);
    }

    @Atomic
    public static void authorize(final Billable billable, final Integer maxCopiesColour, final Money maxValue) {
        final JsonObject configuration = new JsonObject();
        configuration.addProperty("maxCopiesColour", maxCopiesColour);
        configuration.addProperty("maxValue", maxValue.exportAsString());
        billable.authorize(configuration);
    }

    @Override
    protected void createServiceRequest(final Unit financer, final JsonElement beneficiaryConfig) {
        final JsonArray array = beneficiaryConfig.getAsJsonArray();
        for (final JsonElement e : array) {
            final JsonObject o = e.getAsJsonObject();
            final DomainObject domainObject = FenixFramework.getDomainObject(o.get("beneficiaryId").getAsString());
            final Beneficiary beneficiary = beneficiaryFor(domainObject);
            if (beneficiary != null) {
                final JsonElement maxValueString = o.get("maxValue");
                final Money maxValue = maxValueString == null
                        || maxValueString.isJsonNull() ? Money.ZERO : new Money(maxValueString.getAsString());
                if (maxValue.isPositive() && !hasActiveBillable(financer, beneficiary)) {
                    final Billable billable = new Billable(this, financer, beneficiary);
                    final JsonObject configuration = new JsonObject();
                    configuration.addProperty("maxValue", maxValue.exportAsString());
                    final String configString = configuration.toString();
                    billable.setConfiguration(configString);
                    billable.log("Subscribed service " + getTitle() + " for unit " + financer.getPresentationName() + " to user " + beneficiary.getPresentationName() + " with configuration " + configString);
                }
            }
        }
    }

    private boolean hasActiveBillable(final Unit financer, final Beneficiary beneficiary) {
        return beneficiary.getBillableSet().stream().anyMatch(b -> b.getUnit() == financer && b.getBillableService() == this
                && b.getBillableStatus() != BillableStatus.REVOKED);
    }

}
