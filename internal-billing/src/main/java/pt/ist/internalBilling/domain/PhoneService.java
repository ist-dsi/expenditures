package pt.ist.internalBilling.domain;

import com.google.gson.JsonObject;

import pt.ist.fenixframework.Atomic;

public class PhoneService extends PhoneService_Base {
    
    PhoneService(final String title, final String description) {
        setTitle(title);
        setDescription(description);
    }
 
}
