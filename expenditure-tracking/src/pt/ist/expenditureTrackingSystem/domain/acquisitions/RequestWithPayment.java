package pt.ist.expenditureTrackingSystem.domain.acquisitions;

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
}
