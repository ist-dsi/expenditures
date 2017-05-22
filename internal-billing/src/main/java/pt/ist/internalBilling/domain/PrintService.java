package pt.ist.internalBilling.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

public class PrintService extends PrintService_Base {

    public static class PrintServiceRequest extends BillableServiceRequest {
        public Money maxValue = Money.ZERO;

        private String toConfig() {
            final JsonObject configuration = new JsonObject();
            configuration.addProperty("maxValue", maxValue.exportAsString());
            return configuration.toString();            
        }

        private static Money maxValueFromConfig(final Billable billable) {
            final JsonObject configuration = billable.getConfigurationAsJson();
            final JsonElement maxValue = configuration.get("maxValue");
            return maxValue != null && !maxValue.isJsonNull() ?
                    Money.importFromString(maxValue.getAsString()) : Money.ZERO;
        }
    }

    PrintService(final String title, final String description) {
        setTitle(title);
        setDescription(description);
    }

    @Override
    protected void createServiceRequest(final BillableServiceRequest serviceRequest) {
        if (serviceRequest instanceof PrintServiceRequest) {
            final PrintServiceRequest printServiceRequest = (PrintServiceRequest) serviceRequest;

            final Unit financer = printServiceRequest.financer;
            final Beneficiary beneficiary = printServiceRequest.beneficiary;
            final Money maxValue = printServiceRequest.maxValue;

            if (maxValue.isPositive() && !hasActiveBillable(financer, beneficiary)) {
                final Billable billable = new Billable(this, financer, beneficiary);
                final String config = printServiceRequest.toConfig();
                billable.setConfiguration(config);
                billable.log("Subscribed service " + getTitle()
                    + " for unit " + financer.getPresentationName()
                    + " to user " + beneficiary.getPresentationName()
                    + " with configuration " + config);
            }
        } else {
            throw new DomainException("InternalBillingResources", "error.incompatible.service.request.type",
                    getClass().getName(), serviceRequest.getClass().getName());
        }
    }

    private boolean hasActiveBillable(final Unit financer, final Beneficiary beneficiary) {
        return beneficiary.getBillableSet().stream().anyMatch(b -> b.getUnit() == financer && b.getBillableService() == this
                && b.getBillableStatus() != BillableStatus.REVOKED);
    }

    public Money authorizedValueFor(final Billable billable) {
        return billable.getBillableService() == this ?
                PrintServiceRequest.maxValueFromConfig(billable) : Money.ZERO;
    }

}
