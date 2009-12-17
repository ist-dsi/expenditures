package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import java.io.InputStream;
import java.math.BigDecimal;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import myorg.domain.util.Money;
import myorg.util.InputStreamUtil;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

public class CreateRefundInvoiceActivityInformation extends ActivityInformation<RefundProcess> {

    private String invoiceNumber;
    private LocalDate invoiceDate;
    private Money value;
    private BigDecimal vatValue;
    private Money refundableValue;
    private RefundItem item;
    private Supplier supplier;
    private transient InputStream inputStream;
    private String filename;
    private String displayName;
    private byte[] bytes;

    public InputStream getInputStream() {
	return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public byte[] getBytes() {
	if (bytes == null) {
	    bytes = InputStreamUtil.consumeInputStream(getInputStream());
	}
	return bytes;
    }

    public CreateRefundInvoiceActivityInformation(RefundProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
	bytes = null;
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

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && getInvoiceDate() != null && getInvoiceNumber() != null && getValue() != null
		&& getVatValue() != null && getRefundableValue() != null && getItem() != null && getSupplier() != null
		&& getInputStream() != null;
    }
}
