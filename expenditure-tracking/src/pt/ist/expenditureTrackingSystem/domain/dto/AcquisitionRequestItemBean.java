package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.organization.DeliveryInfo;
import pt.ist.expenditureTrackingSystem.domain.util.Address;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.fenixWebFramework.util.DomainReference;

public class AcquisitionRequestItemBean implements Serializable {
    private String description;
    private Integer quantity;
    private Money unitValue;
    private BigDecimal vatValue;
    private String proposalReference;
    private DomainReference<AcquisitionRequest> acquisitionRequest;
    private DomainReference<AcquisitionRequestItem> item;
    private String recipient;
    private Address address;
    private DomainReference<DeliveryInfo> deliveryInfo;
    private CreateItemSchemaType createItemSchemaType;
    private DomainReference<CPVReference> cPVReference;
    
    private Integer realQuantity;
    private Money realUnitValue;
    private Money shipment; 
    
    public AcquisitionRequestItemBean(final AcquisitionRequest acquisitionRequest) {
	setAcquisitionRequest(acquisitionRequest);
	setDeliveryInfo(null);
	setItem(null);
	setCPVReference(null);
	if (acquisitionRequest.getRequester().getDeliveryInfosSet().isEmpty()) {
	    setCreateItemSchemaType(CreateItemSchemaType.NEW_DELIVERY_INFO);
	} else {
	    setCreateItemSchemaType(CreateItemSchemaType.EXISTING_DELIVERY_INFO);
	}
    }

    public AcquisitionRequestItemBean(AcquisitionRequestItem acquisitionRequestItem) {
	this(acquisitionRequestItem.getAcquisitionRequest());
	setDescription(acquisitionRequestItem.getDescription());
	setQuantity(acquisitionRequestItem.getQuantity());
	setUnitValue(acquisitionRequestItem.getUnitValue());
	setProposalReference(acquisitionRequestItem.getProposalReference());
	setVatValue(acquisitionRequestItem.getVatValue());
	setItem(acquisitionRequestItem);
	setRecipient(acquisitionRequestItem.getRecipient());
	setAddress(acquisitionRequestItem.getAddress());
	setCPVReference(acquisitionRequestItem.getCPVReference());
	setDeliveryInfo(acquisitionRequestItem.getAcquisitionRequest().getRequester().getDeliveryInfoByRecipientAndAddress(
		acquisitionRequestItem.getRecipient(), acquisitionRequestItem.getAddress()));
	setCreateItemSchemaType(CreateItemSchemaType.EXISTING_DELIVERY_INFO);
	setRealQuantity(acquisitionRequestItem.getRealQuantity() != null ? acquisitionRequestItem.getRealQuantity() : acquisitionRequestItem.getQuantity());
	setRealUnitValue(acquisitionRequestItem.getRealUnitValue() != null ? acquisitionRequestItem.getRealUnitValue()  : acquisitionRequestItem.getUnitValue());
	setShipment(acquisitionRequestItem.getShipmentValue() != null ? acquisitionRequestItem.getShipmentValue() : Money.ZERO);
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

    public Money getUnitValue() {
	return unitValue;
    }

    public void setUnitValue(Money unitValue) {
	this.unitValue = unitValue;
    }

    public String getProposalReference() {
	return proposalReference;
    }

    public void setProposalReference(String proposalReference) {
	this.proposalReference = proposalReference;
    }

    public AcquisitionRequestItem getItem() {
	return item.getObject();
    }

    public void setItem(AcquisitionRequestItem item) {
	this.item = new DomainReference<AcquisitionRequestItem>(item);
    }

    public Address getAddress() {
	return address;
    }

    public void setAddress(Address address) {
	this.address = address;
    }

    public CreateItemSchemaType getCreateItemSchemaType() {
	return createItemSchemaType;
    }

    public void setCreateItemSchemaType(CreateItemSchemaType createItemSchemaType) {
	this.createItemSchemaType = createItemSchemaType;
    }

    public String getRecipient() {
	return recipient;
    }

    public void setRecipient(String recipient) {
	this.recipient = recipient;
    }

    public DeliveryInfo getDeliveryInfo() {
	return deliveryInfo.getObject();
    }

    public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
	this.deliveryInfo = new DomainReference<DeliveryInfo>(deliveryInfo);
    }

    public static enum CreateItemSchemaType {
	NEW_DELIVERY_INFO, EXISTING_DELIVERY_INFO;
    }

    public BigDecimal getVatValue() {
	return vatValue;
    }

    public void setVatValue(BigDecimal vatValue) {
	this.vatValue = vatValue;
    }

    public Integer getRealQuantity() {
        return realQuantity;
    }

    public void setRealQuantity(Integer realQuantity) {
        this.realQuantity = realQuantity;
    }

    public Money getRealUnitValue() {
        return realUnitValue;
    }

    public void setRealUnitValue(Money realUnitValue) {
        this.realUnitValue = realUnitValue;
    }

    public Money getShipment() {
        return shipment;
    }

    public void setShipment(Money shippement) {
        this.shipment = shippement;
    }

    public CPVReference getCPVReference() {
        return cPVReference.getObject();
    }

    public void setCPVReference(CPVReference reference) {
        cPVReference = new DomainReference<CPVReference>(reference);
    }

}
