package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class UnitItem extends UnitItem_Base {
    
    public UnitItem(Unit unit, AcquisitionRequestItem item, BigDecimal shareValue, Boolean isApproved) {
	setUnit(unit);
	setItem(item);
	setShareValue(shareValue);
	setItemApproved(isApproved);
    }
}
