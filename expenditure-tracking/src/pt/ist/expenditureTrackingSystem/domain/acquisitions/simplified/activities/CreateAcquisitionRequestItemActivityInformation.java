package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.math.BigDecimal;
import java.util.ResourceBundle;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import myorg.domain.util.Address;
import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionItemClassification;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.DeliveryInfo;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class CreateAcquisitionRequestItemActivityInformation extends ActivityInformation<RegularAcquisitionProcess> {

    public static enum CreateItemSchemaType implements IPresentableEnum {
	NEW_DELIVERY_INFO, EXISTING_DELIVERY_INFO;

	@Override
	public String getLocalizedName() {
	    try {
		final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ExpenditureEnumerationResources",
			Language.getLocale());
		return resourceBundle.getString(CreateItemSchemaType.class.getSimpleName() + "." + name());
	    } catch (Exception ex) {
		ex.printStackTrace();
		throw new Error(ex);
	    }
	}
    }

    public static final int DEFAULT_VAL_VALUE = 23;

    private String description;
    private Integer quantity;
    private Money unitValue;
    private BigDecimal vatValue = new BigDecimal(DEFAULT_VAL_VALUE);
    private Money additionalCostValue;
    private String proposalReference;
    private AcquisitionRequest acquisitionRequest;
    private String recipient;
    private String phone;
    private String email;
    private Address address;
    private DeliveryInfo deliveryInfo;
    private AcquisitionItemClassification classification;
    private CreateItemSchemaType createItemSchemaType;
    private CPVReference cPVReference;

    public CreateAcquisitionRequestItemActivityInformation(RegularAcquisitionProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
	setAcquisitionRequest(process.getAcquisitionRequest());
	setDeliveryInfo(null);
	setCPVReference(null);
	if (process.getAcquisitionRequest().getRequester().getDeliveryInfosSet().isEmpty()) {
	    setCreateItemSchemaType(CreateItemSchemaType.NEW_DELIVERY_INFO);
	} else {
	    setCreateItemSchemaType(CreateItemSchemaType.EXISTING_DELIVERY_INFO);
	}
    }

    public void setAcquisitionRequest(final AcquisitionRequest acquisitionRequest) {
	this.acquisitionRequest = acquisitionRequest;
    }

    public AcquisitionRequest getAcquisitionRequest() {
	return acquisitionRequest;
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

    public Money getAdditionalCostValue() {
	return additionalCostValue;
    }

    public void setAdditionalCostValue(Money additionalCostValue) {
	this.additionalCostValue = additionalCostValue;
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
	return deliveryInfo;
    }

    public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
	this.deliveryInfo = deliveryInfo;
    }

    public BigDecimal getVatValue() {
	return vatValue;
    }

    public void setVatValue(BigDecimal vatValue) {
	this.vatValue = vatValue;
    }

    public CPVReference getCPVReference() {
	return cPVReference;
    }

    public void setCPVReference(CPVReference reference) {
	cPVReference = reference;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public String getPhone() {
	return phone;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getEmail() {
	return email;
    }

    public void setClassification(AcquisitionItemClassification classification) {
	this.classification = classification;
    }

    public AcquisitionItemClassification getClassification() {
	return classification;
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && getAcquisitionRequest() != null && getDescription() != null && getQuantity() != null
		&& getUnitValue() != null && getProposalReference() != null && getCPVReference() != null
		&& (getDeliveryInfo() != null || (getRecipient() != null && getAddress() != null));
    }
}
