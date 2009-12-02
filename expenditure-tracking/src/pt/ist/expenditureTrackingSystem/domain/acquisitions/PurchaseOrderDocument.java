package pt.ist.expenditureTrackingSystem.domain.acquisitions;

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

}
