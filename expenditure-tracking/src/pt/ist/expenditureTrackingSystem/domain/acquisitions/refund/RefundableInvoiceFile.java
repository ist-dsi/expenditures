package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

public class RefundableInvoiceFile extends RefundableInvoiceFile_Base {

    public RefundableInvoiceFile(RefundInvoice invoice, byte[] invoiceFile, String filename) {
	super();
	setFilename(filename);
	setInvoice(invoice);
	setContent(invoiceFile);
    }

}
