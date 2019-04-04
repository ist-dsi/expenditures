package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import org.joda.time.LocalDate;

import module.finance.util.Money;

public class Payment extends Payment_Base {

    public Payment(final RequestWithPayment requestWithPayment, final String reference, Money value, Money additionalValue,
            LocalDate date, String description) {
        super();
        setRequestWithPayment(requestWithPayment);
        edit(reference, value, additionalValue, date, description);
    }

    public void edit(String reference, Money value, Money additionalValue, LocalDate date, String description) {
        setReference(reference);
        setValue(value);
        setAdditionalValue(additionalValue);
        setDate(date);
        setDescription(description);
    }

}
