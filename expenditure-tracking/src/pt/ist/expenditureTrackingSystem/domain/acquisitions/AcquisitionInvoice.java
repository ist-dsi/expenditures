package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.fenixWebFramework.services.Service;

public class AcquisitionInvoice extends AcquisitionInvoice_Base {

    public AcquisitionInvoice() { // final RequestWithPayment request) {
	super();
	//setAcquisition(request);
    }

    @Override
    @Service
    public void delete() {
	//removeAcquisition();
	super.delete();
    }
}
