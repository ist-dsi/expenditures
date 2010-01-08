package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import myorg.domain.util.Money;

public class EditAcquisitionRequestItemRealValuesActivityInformation extends ActivityInformation<RegularAcquisitionProcess> {

    private AcquisitionRequestItem item;
    private Integer realQuantity;
    private Money realUnitValue;
    private Money shipment;
    private BigDecimal realVatValue;

    public EditAcquisitionRequestItemRealValuesActivityInformation(RegularAcquisitionProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    public AcquisitionRequestItem getItem() {
	return item;
    }

    public void setItem(AcquisitionRequestItem item) {
	this.item = item;
	setRealQuantity(item.getRealQuantity());
	setRealUnitValue(item.getRealUnitValue());
	setShipment(item.getRealAdditionalCostValue());
	setRealVatValue(item.getRealVatValue());
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

    public void setShipment(Money shipment) {
	this.shipment = shipment;
    }

    public BigDecimal getRealVatValue() {
	return realVatValue;
    }

    public void setRealVatValue(BigDecimal realVatValue) {
	this.realVatValue = realVatValue;
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && getItem() != null && getRealQuantity() != null && getRealUnitValue() != null
		&& getRealVatValue() != null;
    }

}
