package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.util.Money;

public class RequestWithPayment extends RequestWithPayment_Base {

    public RequestWithPayment() {
	super();
    }

    public boolean hasAnyRequestItems() {
	return getRequestItems().size() > 0;
    }

    public Money getTotalValue() {
	Money money = Money.ZERO;
	for (RequestItem item : getRequestItems()) {
	    money = money.add(item.getValue());
	}
	return money;
    }

    public Money getRealTotalValue() {
	Money money = Money.ZERO;
	for (RequestItem item : getRequestItems()) {
	    money = money.add(item.getRealValue());
	}
	return money;
    }

    public boolean hasBeenApprovedBy(final Person person) {
	for (final RequestItem requestItem : getRequestItemsSet()) {
	    if (requestItem.hasBeenApprovedBy(person)) {
		return true;
	    }
	}
	return false;
    }

    public void submittedForFundsAllocation(final Person person) {
	for (final RequestItem requestItem : getRequestItemsSet()) {
	    requestItem.submittedForFundsAllocation(person);
	}
    }

    public boolean isSubmittedForFundsAllocationByAllResponsibles() {
	for (final RequestItem requestItem : getRequestItemsSet()) {
	    if (!requestItem.isApproved()) {
		return false;
	    }
	}
	return true;
    }


}
