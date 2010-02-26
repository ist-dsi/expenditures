package module.workingCapital.presentationTier.provider;

import module.workingCapital.domain.WorkingCapitalSystem;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class WorkingCapitalYearProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object arg0, Object arg1) {
	return WorkingCapitalSystem.getInstance().getWorkingCapitalYears();
    }

}
