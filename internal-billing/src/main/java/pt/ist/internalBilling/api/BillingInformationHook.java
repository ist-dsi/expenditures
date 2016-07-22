package pt.ist.internalBilling.api;

import org.fenixedu.bennu.core.domain.User;

import com.google.gson.JsonObject;

import pt.ist.internalBilling.domain.Billable;

public interface BillingInformationHook {

    void addInfoFor(final JsonObject configuration, final Billable billable);

    void addInfoFor(final JsonObject configuration, final User user);

}
