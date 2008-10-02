package pt.ist.expenditureTrackingSystem.persistenceTier;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class SQL2JavaConverters {
    public static FieldConversion TimeStamp2DateTimeFieldConversion = new TimeStamp2DateTimeFieldConversion();
    public static FieldConversion JavaByteArray2SqlByteArrayFieldConversion = new JavaByteArray2SqlByteArrayFieldConversion();
    public static FieldConversion JavaMoney2SqlMoneyFieldConversion = new JavaMoney2SqlMoneyFieldConversion();
    public static FieldConversion BigDecimalConverter = new BigDecimalConverter();
    public static FieldConversion AddressConverter = new AddressConverter();
    public static FieldConversion LocalDate2SqlFieldConverter = new LocalDate2SqlFieldConverter();
    public static FieldConversion StringsConverter = new StringsConverter();
}
