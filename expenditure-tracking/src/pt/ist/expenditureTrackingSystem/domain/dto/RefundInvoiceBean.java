package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.expenditureTrackingSystem.presentationTier.util.FileUploadBean;
import pt.ist.fenixWebFramework.util.DomainReference;

public class RefundInvoiceBean extends FileUploadBean implements Serializable {

    private Integer invoiceNumber;
    private LocalDate invoiceDate;
    private Money value;
    private BigDecimal vatValue;
    private Money refundableValue;
    private DomainReference<RefundItem> item;
    private DomainReference<Supplier> supplier;

    public RefundInvoiceBean() {
	super();
	setItem(null);
	setSupplier(null);
    }

    public Integer getInvoiceNumber() {
	return invoiceNumber;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
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
	return item.getObject();
    }

    public void setItem(RefundItem item) {
	this.item = new DomainReference<RefundItem>(item);
    }

    public Supplier getSupplier() {
        return supplier.getObject();
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = new DomainReference<Supplier>(supplier);
    }

}
