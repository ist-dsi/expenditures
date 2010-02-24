package module.workingCapital.presentationTier.provider;

import module.workingCapital.domain.AcquisitionClassification;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AcquisitionClassificationProvider  implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(final Object source, final Object currentValue) {
	return AcquisitionClassification.getAvailableClassifications();
    }

}
