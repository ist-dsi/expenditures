package pt.ist.internalBilling;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.fenixedu.bennu.core.domain.User;

import com.google.gson.JsonObject;

import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.internalBilling.domain.Billable;

public interface BillingInformationHook {

    public final static Set<BillingInformationHook> HOOKS = ConcurrentHashMap.newKeySet();

    void addInfoFor(final JsonObject configuration, final Billable billable);

    void addInfoFor(final JsonObject configuration, final User user);

    void authorize(final Billable b);

    void revoke(final Billable b);

    void signalUnitChange(final User user, final Unit unit);

}
