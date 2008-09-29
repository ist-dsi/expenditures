package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.File;
import pt.ist.fenixframework.pstm.Transaction;

public class AcquisitionRequestDocument extends AcquisitionRequestDocument_Base {

    protected AcquisitionRequestDocument(Integer requestNumber) {
	super();
	setRequestNumber(requestNumber);
    }

    public AcquisitionRequestDocument(final AcquisitionRequest acquisitionRequest, final byte[] contents, final String fileName,
	    Integer requestNumber) {
	this(requestNumber);
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

    public static Integer readNextRequestNumber() {
	int requestNumber = 0;
	for (File file : ExpenditureTrackingSystem.getInstance().getFilesSet()) {
	    if (file instanceof AcquisitionRequestDocument) {
		AcquisitionRequestDocument acquisitionRequestDocument = (AcquisitionRequestDocument) file;
		if (acquisitionRequestDocument.getRequestNumber() != null
			&& acquisitionRequestDocument.getRequestNumber() > requestNumber) {
		    requestNumber = acquisitionRequestDocument.getRequestNumber();
		}
	    }
	}
	return ++requestNumber;
    }

}
