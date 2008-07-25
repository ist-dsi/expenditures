package pt.ist.expenditureTrackingSystem.persistenceTier;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import pt.ist.expenditureTrackingSystem.domain.util.MonitaryValue;

public class JavaMonitaryValue2SqlMonitaryValueFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof MonitaryValue) {
            final MonitaryValue monitaryValue = (MonitaryValue) source;
            return monitaryValue.serialize();
        }
        return source;
    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {      
            final String string = (String) source;
            return MonitaryValue.deserialize(string);
        }
        return source;
    }

}
