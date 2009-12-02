package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundableInvoiceFile;

public class EditRefundInvoiceBean extends RefundInvoiceBean implements Serializable {

    private RefundableInvoiceFile invoice;

    public EditRefundInvoiceBean(RefundableInvoiceFile invoice) {
	setInvoice(invoice);
	setInvoiceDate(invoice.getInvoiceDate());
	setInvoiceNumber(invoice.getInvoiceNumber());
	setSupplier(invoice.getSupplier());
	setRefundableValue(invoice.getRefundableValue());
	setValue(invoice.getValue());
	setVatValue(invoice.getVatValue());
    }

    public RefundableInvoiceFile getInvoice() {
	return invoice;
    }

    public void setInvoice(RefundableInvoiceFile invoice) {
	this.invoice = invoice;
    }

}
