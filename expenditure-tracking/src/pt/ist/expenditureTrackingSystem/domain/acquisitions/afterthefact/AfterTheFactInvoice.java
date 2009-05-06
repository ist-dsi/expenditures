package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact;

import pt.ist.fenixWebFramework.services.Service;

public class AfterTheFactInvoice extends AfterTheFactInvoice_Base {

    public AfterTheFactInvoice(final AcquisitionAfterTheFact acquisition) {
	super();
	setAcquisition(acquisition);
    }

    @Override
    @Service
    public void delete() {
	removeAcquisition();
	super.delete();
    }
}
