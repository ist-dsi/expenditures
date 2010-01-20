package pt.ist.expenditureTrackingSystem.presentationTier.renderers.dataProvider;

import java.util.Arrays;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AcquisitionProcessStateTypeProvider implements DataProvider {

    public Converter getConverter() {
	return null;
    }

    public Object provide(Object arg0, Object arg1) {
	return Arrays.asList(AcquisitionProcessStateType.values());
    }

}
