package pt.ist.internalBilling.domain;

import java.util.Comparator;

import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

public class BillableLog extends BillableLog_Base {
    
    public static final Comparator<? super BillableLog> COMPARATOR_BY_WHEN = new Comparator<BillableLog>() {

        @Override
        public int compare(final BillableLog b1, final BillableLog b2) {
            final int c = b1.getWhenInstant().compareTo(b2.getWhenInstant());
            return c == 0 ? b1.getExternalId().compareTo(b2.getExternalId()) : c;
        }

    };

    public BillableLog(final Billable billable, final String description) {
        super();
        setWhenInstant(new DateTime());
        setUser(Authenticate.getUser());
        setBillable(billable);
        setDescription(description);
    }
    
}
