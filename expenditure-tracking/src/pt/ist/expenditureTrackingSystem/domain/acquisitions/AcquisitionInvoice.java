package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.FileContent;
import pt.ist.fenixWebFramework.services.Service;

public class AcquisitionInvoice extends AcquisitionInvoice_Base {

    public AcquisitionInvoice() {
	super();
    }

    @Override
    @Service
    public void delete() {
	super.delete();
    }
}
