package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundableInvoiceFile;
import pt.ist.fenixWebFramework.util.DomainReference;

public class EditRefundInvoiceBean extends RefundInvoiceBean implements Serializable {

    private DomainReference<RefundInvoice> invoice;
    private DomainReference<RefundableInvoiceFile> file;

    public EditRefundInvoiceBean(RefundInvoice invoice) {
	setInvoice(invoice);
	setFile(invoice.getFile());
	setInvoiceDate(invoice.getInvoiceDate());
	setInvoiceNumber(invoice.getInvoiceNumber());
	setSupplier(invoice.getSupplier());
	setRefundableValue(invoice.getRefundableValue());
	setValue(invoice.getValue());
	setVatValue(invoice.getVatValue());
    }

    public RefundInvoice getInvoice() {
	return invoice.getObject();
    }

    public void setInvoice(RefundInvoice invoice) {
	this.invoice = new DomainReference<RefundInvoice>(invoice);
    }

    public RefundableInvoiceFile getFile() {
	return file.getObject();
    }

    public void setFile(RefundableInvoiceFile file) {
	this.file = new DomainReference<RefundableInvoiceFile>(file);
    }

    
}
