package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import myorg.util.ClassNameBundle;

@ClassNameBundle(bundle = "resources/AcquisitionResources")
public class PurchaseOrderDocument extends PurchaseOrderDocument_Base {

    protected PurchaseOrderDocument(String requestId) {
	super();
	setRequestId(requestId);
    }

    public PurchaseOrderDocument(final AcquisitionProcess process, final byte[] contents, final String fileName, String requestID) {
	this(requestID);
	if (process.hasPurchaseOrderDocument()) {
	    process.getPurchaseOrderDocument().delete();
	}

	setProcess(process);
	setContent(contents);
	setFilename(fileName);
    }

    @Override
    public void delete() {
	removeProcess();
	super.delete();
    }

    @Override
    public boolean isPossibleToArchieve() {
	return false;
    }

    @Override
    public String getDisplayName() {
	return getFilename();
    }

    @Override
    public boolean isConnectedToCurrentHost() {
	final GenericProcess genericProcess = (GenericProcess) getProcess();
	return genericProcess != null && genericProcess.isConnectedToCurrentHost();
    }

}
