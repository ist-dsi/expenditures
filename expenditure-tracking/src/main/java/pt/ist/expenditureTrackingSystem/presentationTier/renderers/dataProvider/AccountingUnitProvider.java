package pt.ist.expenditureTrackingSystem.presentationTier.renderers.dataProvider;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AccountingUnitProvider implements DataProvider {

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    public Object provide(Object arg0, Object arg1) {
        return ExpenditureTrackingSystem.getInstance().getAccountingUnits();
    }

}
