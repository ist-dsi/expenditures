package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.fenixWebFramework.util.DomainReference;

public class UnitItemBean implements Serializable {

    private DomainReference<Unit> unit;
    private DomainReference<AcquisitionRequestItem> item;
    private Money shareValue;
    private Boolean approved;
    private Boolean assigned;

    public UnitItemBean(Unit unit, AcquisitionRequestItem item) {
	setItem(item);
	setUnit(unit);
	setAssigned(Boolean.FALSE);

	UnitItem unitItem = item.getUnitItemFor(unit);
	if (unitItem != null) {
	    setAssigned(Boolean.TRUE);
	    setApproved(unitItem.getItemApproved());
	    setShareValue(unitItem.getShareValue());
	}
    }

    public Unit getUnit() {
	return unit.getObject();
    }

    public void setUnit(Unit unit) {
	this.unit = new DomainReference<Unit>(unit);
    }

    public AcquisitionRequestItem getItem() {
	return item.getObject();
    }

    public void setItem(AcquisitionRequestItem item) {
	this.item = new DomainReference<AcquisitionRequestItem>(item);
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
}
