package pt.ist.expenditureTrackingSystem.presentationTier.renderers.dataProvider;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SelectSupplierActivityInformation;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class SuppliersForRegularAcquisitionProcess implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	SelectSupplierActivityInformation information = (SelectSupplierActivityInformation) source;
	return information.getProcess().getRequest().getSuppliers();
    }

}
