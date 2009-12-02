package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact;

import pt.ist.fenixWebFramework.services.Service;

public class AfterTheFactInvoice extends AfterTheFactInvoice_Base {

    public AfterTheFactInvoice(final AfterTheFactAcquisitionProcess process) {
	super();
	process.addFiles(this);
    }

    @Override
    @Service
    public void delete() {
	removeProcess();
	super.delete();
    }
}
