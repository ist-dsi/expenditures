package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.fenixWebFramework.util.DomainReference;

public class CreateAcquisitionRequestItemBean implements Serializable {
    private String description;
    private Integer quantity;
    private BigDecimal unitValue;
    private String proposalReference;
    private String salesCode;
    private DomainReference<AcquisitionRequest> acquisitionRequest;

    public CreateAcquisitionRequestItemBean(final AcquisitionRequest acquisitionRequest) {
	setAcquisitionRequest(acquisitionRequest);
    }

    public void setAcquisitionRequest(final AcquisitionRequest acquisitionRequest) {
	this.acquisitionRequest = acquisitionRequest != null ? new DomainReference<AcquisitionRequest>(acquisitionRequest) : null;
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

}
