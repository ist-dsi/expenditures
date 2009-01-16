package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

import java.math.BigDecimal;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.util.Money;

public class RefundInvoice extends RefundInvoice_Base {

    public RefundInvoice(Integer invoiceNumber, LocalDate invoiceDate, Money value, BigDecimal vatValue, Money refundableValue,
	    byte[] invoiceFile, RefundItem item, Supplier supplier) {
	super();
	this.setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	this.setInvoiceNumber(invoiceNumber);
	this.setInvoiceDate(invoiceDate);
	this.setValue(value);
	this.setVatValue(vatValue);
	this.setRefundableValue(refundableValue);
	this.setRefundItem(item);
	this.setSupplier(supplier);
	new RefundableInvoiceFile(this, invoiceFile);
    }

}
