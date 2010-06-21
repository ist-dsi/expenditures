package module.workingCapital.presentationTier.provider;

import module.workingCapital.domain.activity.ChangeAccountingUnitActivityInformation;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ChangeAccountingUnitProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	ChangeAccountingUnitActivityInformation changeAccountingUnitActivityInformation = (ChangeAccountingUnitActivityInformation) source;
	return changeAccountingUnitActivityInformation.getAccountingUnits();
    }

}
