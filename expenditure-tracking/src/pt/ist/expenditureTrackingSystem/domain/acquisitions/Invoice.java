package pt.ist.expenditureTrackingSystem.domain.acquisitions;


public class Invoice extends Invoice_Base {
    
    public Invoice() {
	super();
    }

    public boolean isInvoiceReceived() {
	return getInvoiceNumber() != null && getInvoiceNumber().length() > 0 && getInvoiceDate() != null;
    }

    public void delete() {
	super.delete();
    }
    
}
