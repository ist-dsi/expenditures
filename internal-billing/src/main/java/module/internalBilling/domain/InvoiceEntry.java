package module.internalBilling.domain;

import pt.ist.bennu.core.domain.util.Money;

public class InvoiceEntry extends InvoiceEntry_Base {
    
    public InvoiceEntry(final Invoice invoice, final BillableThing billableThing, final Money value) {
	super();
	setInvoice(invoice);
	setBillableThing(billableThing);
	setValue(value);
	setVerified(Boolean.FALSE);
    }
    
}
