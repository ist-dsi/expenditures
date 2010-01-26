package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class UnitItemBean implements Serializable {

    private Unit unit;
    private RequestItem item;
    private Money shareValue;
    private Money realShareValue;
    private Boolean approved;
    private Boolean assigned;

    public UnitItemBean(UnitItem unitItem) {
	setUnit(unitItem.getUnit());
	setItem(unitItem.getItem());
	setShareValue(unitItem.getShareValue());
	setRealShareValue(unitItem.getRealShareValue());
	setAssigned(Boolean.TRUE);
    }

    public UnitItemBean(Unit unit, RequestItem item) {
	setItem(item);
	setUnit(unit);
	setAssigned(Boolean.FALSE);

	UnitItem unitItem = item.getUnitItemFor(unit);
	if (unitItem != null) {
	    setAssigned(Boolean.TRUE);
	    setApproved(unitItem.getItemAuthorized());
	    setShareValue(unitItem.getShareValue());
	    setRealShareValue(unitItem.getRealShareValue());
	}
    }

    public Unit getUnit() {
	return unit;
    }

    public void setUnit(Unit unit) {
	this.unit = unit;
    }

    public RequestItem getItem() {
	return item;
    }

    public void setItem(RequestItem item) {
	this.item = item;
    }

    public Money getShareValue() {
	return shareValue;
    }

    public void setShareValue(Money shareValue) {
	this.shareValue = shareValue;
    }

    public Boolean getApproved() {
	return approved;
    }

    public void setApproved(Boolean isApproved) {
	this.approved = isApproved;
    }

    public Boolean getAssigned() {
	return assigned;
    }

    public void setAssigned(Boolean assigned) {
	this.assigned = assigned;
    }

    public Money getRealShareValue() {
	return realShareValue;
    }

    public void setRealShareValue(Money realShareValue) {
	this.realShareValue = realShareValue;
    }
}
