package pt.ist.internalBilling.domain;

import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixframework.Atomic;

public abstract class BillableService extends BillableService_Base {

    public static class BillableServiceRequest {
        public Unit financer;
        public Beneficiary beneficiary;
    }

    protected BillableService() {
        setInternalBillingService(InternalBillingService.getInstance());
    }

    @Atomic
    public static void create(final String type, final String title, final String description,
            final Money costPerBlackAndWhiteCopy, final Money costPerColourCopy) {
        switch (type) {
        case "pt.ist.internalBilling.domain.PhoneService":
            new PhoneService(title, description);
            break;
        case "pt.ist.internalBilling.domain.PrintService":
            new PrintService(title, description);
            break;
        case "pt.ist.internalBilling.domain.VirtualHostingService":
            new VirtualHostingService(title, description);
            break;
        default:
            throw new Error("Unknown billing service type: " + type);
        }
    }

    @Atomic
    public void delete() {
        disconnectForDelete();
        deleteDomainObject();
    }

    protected void disconnectForDelete() {
        setInternalBillingService(null);
    }

    @Atomic
    public void edit(final String title, final String description) {
        setTitle(title);
        setDescription(description);
    }

    @Atomic
    public void request(final BillableServiceRequest... serviceRequests) {
        for (final BillableServiceRequest serviceRequest : serviceRequests) {
            if (serviceRequest != null && serviceRequest.financer != null && serviceRequest.beneficiary != null) {
                createServiceRequest(serviceRequest);
            }
        }
    }

    protected abstract void createServiceRequest(final BillableServiceRequest serviceRequest);

}
