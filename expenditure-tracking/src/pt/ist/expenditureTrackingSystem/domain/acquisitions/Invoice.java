package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.fenixframework.pstm.Transaction;

public class Invoice extends Invoice_Base {
    
    public Invoice(final Acquisition acquisition) {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setAcquisition(acquisition);
    }

    public boolean isInvoiceReceived() {
	return getInvoiceNumber() != null && getInvoiceNumber().length() > 0 && getInvoiceDate() != null;
    }

    public void delete() {
	removeAcquisition();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }
    
}
