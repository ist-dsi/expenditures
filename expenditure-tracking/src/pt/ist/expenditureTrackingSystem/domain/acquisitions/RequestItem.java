package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.util.Money;

public abstract class RequestItem extends RequestItem_Base {

    public RequestItem() {
	super();
	setOjbConcreteClass(getClass().getName());
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public abstract Money getValue();

    public abstract Money getRealValue();

    public abstract BigDecimal getVatValue();

    public Money getTotalAssigned() {
	Money sum = Money.ZERO;
	for (UnitItem unitItem : getUnitItems()) {
	    sum = sum.add(unitItem.getShareValue());
	}
	return sum;
    }

    public Money getTotalRealAssigned() {
	Money sum = Money.ZERO;
	for (UnitItem unitItem : getUnitItems()) {
	    if (unitItem.getRealShareValue() != null) {
		sum = sum.add(unitItem.getRealShareValue());
	    }
	}
	return sum;
    }

}
