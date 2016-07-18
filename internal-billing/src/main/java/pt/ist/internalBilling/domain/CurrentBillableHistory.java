package pt.ist.internalBilling.domain;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

public class CurrentBillableHistory extends CurrentBillableHistory_Base {

    CurrentBillableHistory(final Billable billable, final User user) {
        setWhenInstant(new DateTime());
        setBillable(billable);
        final CurrentBillableHistory previous = user.getCurrentBillableHistory();
        setPreviouseHistory(previous);
        setUser(user);
    }

}
