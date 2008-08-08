package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.fenixWebFramework.util.DomainReference;

public class AcquisitionRequestItemBean implements Serializable {
    private String description;
    private Integer quantity;
    private BigDecimal unitValue;
    private BigDecimal vatValue;
    private String proposalReference;
    private String salesCode;
    private DomainReference<AcquisitionRequest> acquisitionRequest;
    private DomainReference<AcquisitionRequestItem> item;
    
    public AcquisitionRequestItemBean(final AcquisitionRequest acquisitionRequest) {
	setAcquisitionRequest(acquisitionRequest);
    }

    public AcquisitionRequestItemBean(AcquisitionRequestItem acquisitionRequestItem) {
	this(acquisitionRequestItem.getAcquisitionRequest());
	setDescription(acquisitionRequestItem.getDescription());
	setQuantity(acquisitionRequestItem.getQuantity());
	setUnitValue(acquisitionRequestItem.getUnitValue());
	setProposalReference(acquisitionRequestItem.getProposalReference());
	setSalesCode(acquisitionRequestItem.getSalesCode());
	setVatValue(acquisitionRequestItem.getVatValue());
	setItem(acquisitionRequestItem);
    }

    public void setAcquisitionRequest(final AcquisitionRequest acquisitionRequest) {
	this.acquisitionRequest = new DomainReference<AcquisitionRequest>(acquisitionRequest);
    }

    public AcquisitionRequest getAcquisitionRequest() {
	return acquisitionRequest != null ? acquisitionRequest.getObject() : null;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Integer getQuantity() {
	return quantity;
    }

    public void setQuantity(Integer quantity) {
	this.quantity = quantity;
    }

    public BigDecimal getUnitValue() {
	return unitValue;
    }

    public void setUnitValue(BigDecimal unitValue) {
	this.unitValue = unitValue;
    }

    public String getProposalReference() {
	return proposalReference;
    }

    public void setProposalReference(String proposalReference) {
	this.proposalReference = proposalReference;
    }

    public String getSalesCode() {
	return salesCode;
    }

    public void setSalesCode(String salesCode) {
	this.salesCode = salesCode;
    }

    public AcquisitionRequestItem getItem() {
        return item.getObject();
    }

    public void setItem(AcquisitionRequestItem item) {
        this.item = new DomainReference<AcquisitionRequestItem>(item);
    }

    public BigDecimal getVatValue() {
        return vatValue;
    }

    public void setVatValue(BigDecimal vatValue) {
        this.vatValue = vatValue;
    }

}
