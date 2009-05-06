package pt.ist.expenditureTrackingSystem.presentationTier.renderers.dataProvider;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions.SimplifiedProcedureProcessAction.ReceiveInvoiceForm;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ItemsFromProcess implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	ReceiveInvoiceForm form = (ReceiveInvoiceForm) source;
	AcquisitionRequest request = form.getRequest();

	return request.getRequestItems();
    }

}
