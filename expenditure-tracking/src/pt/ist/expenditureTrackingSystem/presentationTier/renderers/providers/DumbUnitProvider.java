package pt.ist.expenditureTrackingSystem.presentationTier.renderers.providers;

import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DumbUnitProvider implements DataProvider {

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    public Object provide(Object arg0, Object arg1) {
	final List<Unit> units = ExpenditureTrackingSystem.getInstance().getUnits();
	return units.size() > 10 ? units.subList(0, 10) : units;
    }

}
