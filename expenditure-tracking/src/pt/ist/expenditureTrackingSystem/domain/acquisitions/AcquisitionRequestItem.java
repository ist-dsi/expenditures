package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.pstm.Transaction;

public class AcquisitionRequestItem extends AcquisitionRequestItem_Base {

    public AcquisitionRequestItem(final AcquisitionRequest acquisitionRequest) {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setAcquisitionRequest(acquisitionRequest);
    }

    public AcquisitionRequestItem(final AcquisitionRequest acquisitionRequest, final String description, final Integer quantity,
	    final BigDecimal unitValue, final String proposalReference, String salesCode) {
	this(acquisitionRequest);
	setDescription(description);
	setQuantity(quantity);
	setUnitValue(unitValue);
	setProposalReference(proposalReference);
	setSalesCode(salesCode);
    }

    public BigDecimal getTotalItemValue() {
	final BigDecimal unitValue = getUnitValue();
	final Integer quantity = getQuantity();
	return multiply(unitValue, quantity);
    }

    public BigDecimal getTotalAssignedValue() {
	BigDecimal sum = BigDecimal.ZERO;
	for (UnitItem unitItem : getUnitItems()) {
	    sum = sum.add(unitItem.getShareValue());
	}
	return sum;
    }
    
    private BigDecimal multiply(final BigDecimal unitValue, final Integer quantity) {
	return unitValue == null || quantity == null ? BigDecimal.ZERO : unitValue.multiply(new BigDecimal(quantity.intValue()));
    }

    public void edit(String description, Integer quantity, BigDecimal unitValue, String proposalReference, String salesCode) {
	setDescription(description);
	setQuantity(quantity);
	setUnitValue(unitValue);
	setProposalReference(proposalReference);
	setSalesCode(salesCode);
    }
    
    @Service
    public void delete() {
	removeAcquisitionRequest();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

    public boolean isAssignedTo(Unit unit) {
	for (UnitItem unitItem : getUnitItems()) {
	    if (unitItem.getUnit() == unit) {
		return true;
	    }
	}
	return false;
    }
    
    public UnitItem getUnitItemFor(Unit unit) {
	for (UnitItem unitItem : getUnitItems()) {
	    if (unitItem.getUnit() == unit) {
		return unitItem;
	    }
	}
	return null;
    }
    
    public boolean isValueFullyAttributedToUnits() {
	BigDecimal totalValue = BigDecimal.ZERO;
	for (UnitItem unitItem : getUnitItems()) {
	    totalValue = totalValue.add(unitItem.getShareValue());
	}
	
	return totalValue.equals(getTotalItemValue());
    }

    public void createUnitItem(Unit unit, BigDecimal shareValue) {
	new UnitItem(unit,this,shareValue,Boolean.FALSE);
    }
}
