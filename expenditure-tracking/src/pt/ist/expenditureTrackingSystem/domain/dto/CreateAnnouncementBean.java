package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.Acquisition;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.fenixWebFramework.util.DomainReference;

public class CreateAnnouncementBean implements Serializable {

    private String description;
    private Money totalPrice;
    private Integer executionDays;
    private String executionAddress;
    private String choiceCriteria;
    private DomainReference<Unit> buyingUnit;
    private DomainReference<Supplier> supplier;
    private DomainReference<Acquisition> acquisition;

    public Unit getBuyingUnit() {
	return (buyingUnit != null ? buyingUnit.getObject() : null);
    }

    public void setBuyingUnit(Unit buyingUnit) {
	this.buyingUnit = new DomainReference<Unit>(buyingUnit);
    }

    public Supplier getSupplier() {
	return (supplier != null ? supplier.getObject() : null);
    }

    public void setSupplier(Supplier supplier) {
	this.supplier = new DomainReference<Supplier>(supplier);
    }

    public Acquisition getAcqusition() {
	return (acquisition != null ? acquisition.getObject() : null);
    }

    public void setAcquisition(Acquisition acquisition) {
	this.acquisition = new DomainReference<Acquisition>(acquisition);
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Money getTotalPrice() {
	return totalPrice;
    }

    public void setTotalPrice(Money totalPrice) {
	this.totalPrice = totalPrice;
    }

    public Integer getExecutionDays() {
	return executionDays;
    }

    public void setExecutionDays(Integer executionDays) {
	this.executionDays = executionDays;
    }

    public String getExecutionAddress() {
	return executionAddress;
    }

    public void setExecutionAddress(String executionAddress) {
	this.executionAddress = executionAddress;
    }

    public String getChoiceCriteria() {
	return choiceCriteria;
    }

    public void setChoiceCriteria(String choiceCriteria) {
	this.choiceCriteria = choiceCriteria;
    }
}
