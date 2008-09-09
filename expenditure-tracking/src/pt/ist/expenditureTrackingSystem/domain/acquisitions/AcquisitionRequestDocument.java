package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRException;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.File;
import pt.ist.expenditureTrackingSystem.util.ReportUtils;
import pt.ist.fenixframework.pstm.Transaction;

public class AcquisitionRequestDocument extends AcquisitionRequestDocument_Base {

    protected AcquisitionRequestDocument() {
	super();
	setRequestNumber(readNextRequestNumber());
    }

    public AcquisitionRequestDocument(final AcquisitionRequest acquisitionRequest, final byte[] contents, final String fileName) {
	this();
	if (acquisitionRequest.hasAcquisitionRequestDocument()) {
	    acquisitionRequest.getAcquisitionRequestDocument().delete();
	}

	setAcquisitionRequest(acquisitionRequest);
	setContent(contents);
	setFilename(fileName);
    }

    public AcquisitionRequestDocument(final AcquisitionRequest acquisitionRequest) {
	this();
	if (acquisitionRequest.hasAcquisitionRequestDocument()) {
	    acquisitionRequest.getAcquisitionRequestDocument().delete();
	}
	setAcquisitionRequest(acquisitionRequest);

	final Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("acquisitionRequest", acquisitionRequest);
	paramMap.put("requestNumber", getRequestNumber());
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources/AcquisitionResources");
	try {
	    byte[] byteArray = ReportUtils.exportToPdfFileAsByteArray("acquisitionRequestDocument", paramMap, resourceBundle,
		    acquisitionRequest.getAcquisitionRequestItemsSet());
	    setContent(byteArray);
	} catch (JRException e) {
	    throw new DomainException("acquisitionRequestDocument.message.exception.failedCreation");
	}
	setFilename(getRequestNumber() + "." + EXTENSION_PDF);
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

    @Override
    protected String guessContentType(String filename) {
	return CONTENT_TYPE_PDF;
    }

}
