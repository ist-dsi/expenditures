package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import java.math.BigDecimal;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundableInvoiceFile;

public class EditRefundInvoiceActivityInformation extends ActivityInformation<RefundProcess> {

    RefundableInvoiceFile invoice;
    Money value;
    BigDecimal vatValue;
    Money refundableValue;

    public EditRefundInvoiceActivityInformation(RefundProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    public RefundableInvoiceFile getInvoice() {
	return invoice;
    }

    public void setInvoice(RefundableInvoiceFile invoice) {
	this.invoice = invoice;
	setValue(invoice.getValue());
	setVatValue(invoice.getVatValue());
	setRefundableValue(invoice.getRefundableValue());
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

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && getValue() != null && getVatValue() != null && getRefundableValue() != null;
    }
}
