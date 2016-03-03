package pt.ist.internalBilling.domain;

import org.fenixedu.bennu.core.domain.User;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;

public class BillableService extends BillableService_Base {

    public BillableService() {
        setInternalBillingService(InternalBillingService.getInstance());
    }

    public JsonObject toJson() {
        final JsonObject j = new JsonObject();
        j.addProperty("id", getExternalId());
        j.addProperty("type", getClass().getName());
        j.addProperty("title", getTitle());
        j.addProperty("description", getDescription());
        return j;
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
        setInternalBillingService(null);
        deleteDomainObject();
    }

    @Atomic
    public void edit(final String title, final String description) {
        setTitle(title);
        setDescription(description);
    }

    @Atomic
    public void request(final Unit financer, final JsonElement beneficiaryConfig) {
        if (financer != null && beneficiaryConfig != null) {
            createServiceRequest(financer, beneficiaryConfig);
        }
    }

    protected void createServiceRequest(final Unit financer, final JsonElement beneficiaryConfig) {
    }

    protected static Beneficiary beneficiaryFor(final DomainObject object) {
        return object instanceof User ? UserBeneficiary.beneficiaryFor((User) object)
                : object instanceof Unit ? UnitBeneficiary.beneficiaryFor((Unit) object) : null;
    }

}
