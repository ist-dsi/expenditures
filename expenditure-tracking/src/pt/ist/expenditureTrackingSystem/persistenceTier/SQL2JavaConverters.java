package pt.ist.expenditureTrackingSystem.persistenceTier;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class SQL2JavaConverters {
    public static FieldConversion TimeStamp2DateTimeFieldConversion = new TimeStamp2DateTimeFieldConversion();
    public static FieldConversion JavaByteArray2SqlByteArrayFieldConversion = new JavaByteArray2SqlByteArrayFieldConversion();
    public static FieldConversion JavaMonitaryValue2SqlMonitaryValueFieldConversion = new JavaMonitaryValue2SqlMonitaryValueFieldConversion();
    public static FieldConversion BigDecimalConverter = new BigDecimalConverter();
}
