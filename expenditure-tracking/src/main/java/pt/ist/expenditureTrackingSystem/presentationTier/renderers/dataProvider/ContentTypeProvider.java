package pt.ist.expenditureTrackingSystem.presentationTier.renderers.dataProvider;

import pt.ist.expenditureTrackingSystem.domain.ContractType;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ContentTypeProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(final Object source, final Object current) {
        return ContractType.orderedTypes();
    }

}
