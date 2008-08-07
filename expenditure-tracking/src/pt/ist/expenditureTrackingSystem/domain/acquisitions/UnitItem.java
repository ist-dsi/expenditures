package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixframework.pstm.Transaction;

public class UnitItem extends UnitItem_Base {

    public UnitItem(Unit unit, AcquisitionRequestItem item, BigDecimal shareValue, Boolean isApproved) {

	checkParameters(unit, item, shareValue, isApproved);

	setUnit(unit);
	setItem(item);
	setShareValue(shareValue);
	setItemApproved(isApproved);
    }

    private void checkParameters(Unit unit, AcquisitionRequestItem item, BigDecimal shareValue, Boolean isApproved) {
	if (unit == null || item == null || shareValue == null || isApproved == null) {
	    throw new DomainException("error.parameters.cannot.be.null");
	}
	
	if (shareValue.equals(BigDecimal.ZERO)) {
	    throw new DomainException("error.share.value.cannot.be.zero");
	}
	
	if(shareValue.compareTo(BigDecimal.ZERO) < 0) {
	    throw new DomainException("error.share.value.cannot.be.negative");
	}
	
	BigDecimal currentAssignedValue = item.getTotalAssignedValue();
	if (currentAssignedValue.add(shareValue).compareTo(item.getTotalItemValue()) > 0) {
	    throw new DomainException("error.assigned.value.bigger.than.total.amount");
	}
    }
    
    public void delete() {
	removeUnit();
	removeItem();
	Transaction.deleteObject(this);
    }
}
