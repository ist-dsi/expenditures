package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionType;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.fenixWebFramework.util.DomainReference;

public class AfterTheFactAcquisitionProcessBean implements Serializable {

    private AfterTheFactAcquisitionType afterTheFactAcquisitionType;
    private DomainReference<Supplier> supplier;
    private Money value;
    private BigDecimal vatValue;
    private DomainReference<AfterTheFactAcquisitionProcess> afterTheFactAcquisitionProcess;

    public AfterTheFactAcquisitionProcessBean() {
    }

    public AfterTheFactAcquisitionProcessBean(final AfterTheFactAcquisitionProcess afterTheFactAcquisitionProcess) {
	setAfterTheFactAcquisitionProcess(afterTheFactAcquisitionProcess);
	final AcquisitionAfterTheFact acquisitionAfterTheFact = afterTheFactAcquisitionProcess.getAcquisitionAfterTheFact();
	setAfterTheFactAcquisitionType(acquisitionAfterTheFact.getAfterTheFactAcquisitionType());
	setSupplier(acquisitionAfterTheFact.getSupplier());
	setValue(acquisitionAfterTheFact.getValue());
	setVatValue(acquisitionAfterTheFact.getVatValue());
    }

    public void setSupplier(Supplier supplier) {
	this.supplier = supplier == null ? null : new DomainReference<Supplier>(supplier);
    }

    public Supplier getSupplier() {
	return supplier == null ? null : supplier.getObject();
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

    public AfterTheFactAcquisitionType getAfterTheFactAcquisitionType() {
        return afterTheFactAcquisitionType;
    }

    public void setAfterTheFactAcquisitionType(AfterTheFactAcquisitionType afterTheFactAcquisitionType) {
        this.afterTheFactAcquisitionType = afterTheFactAcquisitionType;
    }

    public AfterTheFactAcquisitionProcess getAfterTheFactAcquisitionProcess() {
        return afterTheFactAcquisitionProcess == null ? null : afterTheFactAcquisitionProcess.getObject();
    }

    public void setAfterTheFactAcquisitionProcess(AfterTheFactAcquisitionProcess afterTheFactAcquisitionProcess) {
        this.afterTheFactAcquisitionProcess = afterTheFactAcquisitionProcess == null ? null : new DomainReference<AfterTheFactAcquisitionProcess>(afterTheFactAcquisitionProcess);
    }

}
