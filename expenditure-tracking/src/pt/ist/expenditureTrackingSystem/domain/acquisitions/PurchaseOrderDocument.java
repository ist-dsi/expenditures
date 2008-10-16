package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.fenixframework.pstm.Transaction;

public class PurchaseOrderDocument extends PurchaseOrderDocument_Base {

    protected PurchaseOrderDocument(String requestId) {
	super();
	setRequestId(requestId);
    }

    public PurchaseOrderDocument(final AcquisitionRequest acquisitionRequest, final byte[] contents, final String fileName,
	    String requestID) {
	this(requestID);
	if (acquisitionRequest.hasPurchaseOrderDocument()) {
	    acquisitionRequest.getPurchaseOrderDocument().delete();
	}

	setAcquisitionRequest(acquisitionRequest);
	setContent(contents);
	setFilename(fileName);
    }

    public void delete() {
	removeAcquisitionRequest();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

}
