package pt.ist.expenditureTrackingSystem.presentationTier.renderers.dataProvider;

import java.util.stream.Collectors;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class RefundItemNatureProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        return ExpenditureTrackingSystem.getInstance().getRefundItemNatureSet().stream()
                .sorted().collect(Collectors.toList());
    }

}
