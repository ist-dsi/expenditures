package pt.ist.expenditureTrackingSystem.domain.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

public class MonitaryValue implements Serializable {

    private static final String SEPERATOR = ":";

    private final Currency currency;
    private final BigDecimal value;

    public MonitaryValue(final Currency currency, final BigDecimal value) {
	this.currency = currency;
	this.value = value;
    }

    public String serialize() {
	final StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(currency.getCurrencyCode());
	stringBuilder.append(SEPERATOR);
	stringBuilder.append(value.toString());
	return stringBuilder.toString();
    }

    public static MonitaryValue deserialize(final String serializedMonitaryValue) {
	final int seperatorIndex = serializedMonitaryValue.indexOf(SEPERATOR);
	final String currencyCode = serializedMonitaryValue.substring(0, seperatorIndex);
	final String valueString = serializedMonitaryValue.substring(seperatorIndex + 1);
	final Currency currency = Currency.getInstance(currencyCode);
	final BigDecimal value = new BigDecimal(valueString);
	return new MonitaryValue(Currency.getInstance(currencyCode), value);
    }

}
