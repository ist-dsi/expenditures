package pt.ist.internalBilling.domain;

import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

public class BillableLog extends BillableLog_Base {
    
    public BillableLog(final Billable billable, final String description) {
        super();
        setWhenInstant(new DateTime());
        setUser(Authenticate.getUser());
        setBillable(billable);
        setDescription(description);
    }
    
}
