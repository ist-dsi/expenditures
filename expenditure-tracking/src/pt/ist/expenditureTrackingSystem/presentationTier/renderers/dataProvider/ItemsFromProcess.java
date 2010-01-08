package pt.ist.expenditureTrackingSystem.presentationTier.renderers.dataProvider;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.fileBeans.InvoiceFileBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ItemsFromProcess implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	InvoiceFileBean form = (InvoiceFileBean) source;
	AcquisitionRequest request = form.getRequest();

	return request.getRequestItems();
    }

}
