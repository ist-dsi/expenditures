package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import myorg.domain.util.Money;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.presentationTier.util.FileUploadBean;

public class RefundInvoiceBean extends FileUploadBean implements Serializable {

    private String invoiceNumber;
    private LocalDate invoiceDate;
    private Money value;
    private BigDecimal vatValue;
    private Money refundableValue;
    private RefundItem item;
    private Supplier supplier;

    public RefundInvoiceBean() {
	super();
	setItem(null);
	setSupplier(null);
    }

    public String getInvoiceNumber() {
	return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
	return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
	this.invoiceDate = invoiceDate;
    }

    public Money getValue() {
	return value;
    }

    public void setValue(Money value) {
	this.value = value;
    }

    public BigDecimal getVatValue() {
	return vatValue;
    }

    public void setVatValue(BigDecimal vatValue) {
	this.vatValue = vatValue;
    }

    public Money getRefundableValue() {
	return refundableValue;
    }

    public void setRefundableValue(Money refundableValue) {
	this.refundableValue = refundableValue;
    }

    public RefundItem getItem() {
	return item;
    }

    public void setItem(RefundItem item) {
	this.item = item;
    }

    public Supplier getSupplier() {
	return supplier;
    }

    public void setSupplier(Supplier supplier) {
	this.supplier = supplier;
    }

}
