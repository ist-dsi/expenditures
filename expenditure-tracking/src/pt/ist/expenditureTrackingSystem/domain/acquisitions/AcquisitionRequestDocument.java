package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.fenixframework.pstm.Transaction;

public class AcquisitionRequestDocument extends AcquisitionRequestDocument_Base {

    protected AcquisitionRequestDocument(String requestId) {
	super();
	setRequestId(requestId);
    }

    public AcquisitionRequestDocument(final AcquisitionRequest acquisitionRequest, final byte[] contents, final String fileName,
	    String requestID) {
	this(requestID);
	if (acquisitionRequest.hasAcquisitionRequestDocument()) {
	    acquisitionRequest.getAcquisitionRequestDocument().delete();
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
