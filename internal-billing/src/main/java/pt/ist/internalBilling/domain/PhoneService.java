package pt.ist.internalBilling.domain;

import com.google.gson.JsonObject;

import pt.ist.fenixframework.Atomic;

public class PhoneService extends PhoneService_Base {
    
    PhoneService(final String title, final String description) {
        setTitle(title);
        setDescription(description);
    }
 
    @Atomic
    public static void authorize(final Billable billable, final String screenName, final Boolean internalCalls, final Boolean nationalHardline,
            final Boolean mobileNetwork, final Boolean internationalCalls) {
        final JsonObject configuration = new JsonObject();
        configuration.addProperty("screenName", screenName);
        configuration.addProperty("internalCalls", internalCalls);
        configuration.addProperty("nationalHardline", nationalHardline);
        configuration.addProperty("mobileNetwork", mobileNetwork);
        configuration.addProperty("internationalCalls", internationalCalls);
        billable.authorize(configuration);
    }

}
