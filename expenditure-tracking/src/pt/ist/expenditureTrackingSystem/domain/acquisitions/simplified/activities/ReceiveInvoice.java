package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.util.List;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;

public class ReceiveInvoice extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	return userHasRole(RoleType.ACQUISITION_CENTRAL);
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return super.isAvailable(process) && process.getAcquisitionProcessState().isAcquisitionProcessed();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	processInvoiceData(process, objects);
	// process.invoiceReceived();
    }

    protected void processInvoiceData(RegularAcquisitionProcess process, Object... objects) {
	final AcquisitionRequest acquisitionRequest = process.getAcquisitionRequest();

	String filename = (String) objects[0];
	byte[] bytes = (byte[]) objects[1];
	String invoiceNumber = (String) objects[2];
	LocalDate invoiceDate = (LocalDate) objects[3];
	List<AcquisitionRequestItem> items = (List<AcquisitionRequestItem>) objects[4];
	Boolean isLastInvoice = (Boolean) objects[5];

	AcquisitionInvoice invoice = acquisitionRequest.receiveInvoice(filename, bytes, invoiceNumber, invoiceDate);

	for (AcquisitionRequestItem item : items) {
	    invoice.addRequestItems(item);
	}

	if (isLastInvoice) {
	    process.invoiceReceived();
	}
    }
}
