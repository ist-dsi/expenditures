package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

public class RefundableInvoiceFile extends RefundableInvoiceFile_Base {

    public RefundableInvoiceFile(RefundInvoice invoice, byte[] invoiceFile) {
	super();
	setInvoice(invoice);
	setContent(invoiceFile);
    }

}
