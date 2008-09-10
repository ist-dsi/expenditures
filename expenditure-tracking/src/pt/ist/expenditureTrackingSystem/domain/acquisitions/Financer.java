package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.fenixframework.pstm.Transaction;

public class Financer extends Financer_Base {

    protected Financer() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public Financer(final AcquisitionRequest acquisitionRequest, final Unit unit) {
	this();
	if (acquisitionRequest == null || unit == null) {
	    throw new DomainException("error.financer.wrong.initial.arguments");
	}
	if (acquisitionRequest.hasPayingUnit(unit)) {
	    throw new DomainException("error.financer.acquisition.request.already.has.paying.unit");
	}

	setFundedRequest(acquisitionRequest);
	setUnit(unit);
    }

    public void delete() {
	if (checkIfCanDelete()) {
	    removeExpenditureTrackingSystem();
	    removeFundedRequest();
	    removeUnit();
	    Transaction.deleteObject(this);
	}
    }

    private boolean checkIfCanDelete() {
	if (hasAnyUnitItems()) {
	    throw new DomainException("acquisitionProcess.message.exception.cannotRemovePayingUnit.alreadyAssignedToItems");
	}
	return true;
    }

    public Money getAmountAllocated() {
	Money amount = Money.ZERO;
	for (UnitItem unitItem : getUnitItems()) {
	    if (unitItem.getRealShareValue() != null) {
		amount = amount.add(unitItem.getRealShareValue());
	    } else if (unitItem.getShareValue() != null) {
		amount = amount.add(unitItem.getShareValue());
	    }
	}
	return amount;
    }

    public Money getRealShareValue() {
	Money amount = Money.ZERO;
	for (UnitItem unitItem : getUnitItemsSet()) {
	    amount = amount.add(unitItem.getRealShareValue());
	}
	return amount;
    }

    public Money getShareValue() {
	Money amount = Money.ZERO;
	for (UnitItem unitItem : getUnitItemsSet()) {
	    amount = amount.add(unitItem.getShareValue());
	}
	return amount;
    }

    public boolean isRealUnitShareValueLessThanUnitShareValue() {
	return getRealShareValue().isLessThanOrEqual(getShareValue());
    }
}
