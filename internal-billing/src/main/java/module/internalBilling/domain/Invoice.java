package module.internalBilling.domain;

import org.joda.time.Interval;
import org.joda.time.LocalDate;

import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.util.Money;

public class Invoice extends Invoice_Base {

    public Invoice(final Account account, final String number, final Interval interval, final LocalDate invoiceDate,
            final LocalDate paymentLimit, final BillableThing[] billableThings, final Money[] values) {
        super();
        setAccount(account);
        setNumber(number);
        setInterval(interval);
        setInvoiceDate(invoiceDate);
        setPaymentLimit(paymentLimit);
        if (billableThings.length != values.length) {
            throw new DomainException("error.number.of.billable.things.does.not.match.values");
        }
        for (int i = 0; i < billableThings.length; i++) {
            final BillableThing billableThing = billableThings[i];
            final Money value = values[i];
            new InvoiceEntry(this, billableThing, value);
        }
    }

}
