package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

public class PaymentProcessYear extends PaymentProcessYear_Base {
    
    private PaymentProcessYear() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setCounter(Integer.valueOf(0));
    }

    private PaymentProcessYear(final Integer year) {
	this();
	setYear(year);
    }

    public Integer nextAcquisitionProcessYearNumber() {
	return getAndUpdateNextCountNumber();
    }

    private Integer getAndUpdateNextCountNumber() {
	setCounter(getCounter().intValue() + 1);
	return getCounter();
    }

    public static PaymentProcessYear getPaymentProcessYearByYear(final Integer year) {
	PaymentProcessYear acquisitionProcessYear = findPaymentProcessYear(year);
	return acquisitionProcessYear != null ? acquisitionProcessYear : new PaymentProcessYear(year);
    }

    private static PaymentProcessYear findPaymentProcessYear(final Integer year) {
	for (PaymentProcessYear paymentProcessYear : ExpenditureTrackingSystem.getInstance().getPaymentProcessYears()) {
	    if (paymentProcessYear.getYear().equals(year)) {
		return paymentProcessYear;
	    }
	}
	return null;
    }

    @Override
    public boolean isConnectedToCurrentHost() {
	return getExpenditureTrackingSystem() == ExpenditureTrackingSystem.getInstance();
    }

}
