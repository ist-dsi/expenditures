package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

public class Invoice extends Invoice_Base {
    
    public Invoice() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public boolean isInvoiceReceived() {
	return getInvoiceNumber() != null && getInvoiceNumber().length() > 0 && getInvoiceDate() != null;
    }

    public void delete() {
	removeExpenditureTrackingSystem();
	super.delete();
    }
    
}
