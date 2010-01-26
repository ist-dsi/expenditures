package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;

public class RefundItemBean implements Serializable {

    private Money valueEstimation;
    private CPVReference reference;
    private String description;

    public RefundItemBean() {
	setCPVReference(null);
    }

    public RefundItemBean(RefundItem item) {
	setValueEstimation(item.getValueEstimation());
	setCPVReference(item.getCPVReference());
	setDescription(item.getDescription());
    }

    public Money getValueEstimation() {
	return valueEstimation;
    }

    public void setValueEstimation(Money valueEstimation) {
	this.valueEstimation = valueEstimation;
    }

    public CPVReference getCPVReference() {
	return reference;
    }

    public void setCPVReference(CPVReference reference) {
	this.reference = reference;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }
}
