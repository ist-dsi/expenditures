package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.util.DomainReference;

public class UnitItemBean implements Serializable {

    private DomainReference<Unit> unit;
    private DomainReference<RequestItem> item;
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
	}
    }

    public Unit getUnit() {
	return unit.getObject();
    }

    public void setUnit(Unit unit) {
	this.unit = new DomainReference<Unit>(unit);
    }

    public RequestItem getItem() {
	return item.getObject();
    }

    public void setItem(RequestItem item) {
	this.item = new DomainReference<RequestItem>(item);
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
