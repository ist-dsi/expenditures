package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.fenixframework.pstm.Transaction;

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

    protected void delete() {
	Transaction.deleteObject(this);
    }

    public UnitItem getUnitItemFor(Unit unit) {
	for (UnitItem unitItem : getUnitItems()) {
	    if (unitItem.getUnit() == unit) {
		return unitItem;
	    }
	}
	return null;
    }

    public void createUnitItem(Financer financer, Money shareValue) {
	new UnitItem(financer, this, shareValue, Boolean.FALSE, Boolean.FALSE);
    }

    public abstract void createUnitItem(Unit unit, Money shareValue);

    public boolean hasBeenApprovedBy(final Person person) {
	for (final UnitItem unitItem : getUnitItems()) {
	    if (unitItem.getUnit().isResponsible(person) && unitItem.getSubmitedForFundsAllocation()) {
		return true;
	    }
	}
	return false;
    }

    public void submittedForFundsAllocation(final Person person) {
	modifySubmittedForFundsAllocationStateFor(person, Boolean.TRUE);
    }

    public void unSubmitForFundsAllocation(final Person person) {
	modifySubmittedForFundsAllocationStateFor(person, Boolean.FALSE);
    }

    private void modifySubmittedForFundsAllocationStateFor(final Person person, final Boolean value) {
	for (final UnitItem unitItem : getUnitItems()) {
	    if (unitItem.getUnit().isResponsible(person)) {
		unitItem.setSubmitedForFundsAllocation(value);
	    }
	}
    }

    public boolean isApproved() {
	for (final UnitItem unitItem : getUnitItems()) {
	    if (!unitItem.getSubmitedForFundsAllocation()) {
		return false;
	    }
	}
	return true;
    }

    public boolean hasBeenAuthorizedBy(final Person person) {
	for (UnitItem unitItem : getUnitItems()) {
	    if (unitItem.getUnit().isResponsible(person) && unitItem.getItemAuthorized()) {
		return true;
	    }
	}
	return false;
    }

    public void authorizeBy(Person person) {
	modifyAuthorizationStateFor(person, Boolean.TRUE);
    }

    public void unathorizeBy(Person person) {
	modifyAuthorizationStateFor(person, Boolean.FALSE);
    }

    private void modifyInvoiceState(Person person, Boolean value) {
	for (UnitItem unitItem : getUnitItems()) {
	    if (unitItem.getUnit().isResponsible(person)) {
		unitItem.setInvoiceConfirmed(value);
	    }
	}
    }

    public void confirmInvoiceBy(Person person) {
	modifyInvoiceState(person, Boolean.TRUE);
    }

    public void unconfirmInvoiceBy(Person person) {
	modifyInvoiceState(person, Boolean.FALSE);
    }

    private void modifyAuthorizationStateFor(Person person, Boolean value) {
	for (UnitItem unitItem : getUnitItems()) {
	    if (unitItem.getUnit().isResponsible(person)) {
		unitItem.setItemAuthorized(value);
	    }
	}
    }

    public boolean isAuthorized() {
	for (UnitItem unitItem : getUnitItems()) {
	    if (!unitItem.getItemAuthorized()) {
		return false;
	    }
	}
	return true;
    }

}
