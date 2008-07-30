package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

public class Invoice extends Invoice_Base {
    
    public Invoice(final AcquisitionRequest acquisitionRequest) {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setAcquisitionRequest(acquisitionRequest);
    }

    public boolean isInvoiceReceived() {
	return getInvoiceNumber() != null && getInvoiceNumber().length() > 0 && getInvoiceDate() != null;
    }
    
}
