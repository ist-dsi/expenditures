package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRException;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.File;
import pt.ist.expenditureTrackingSystem.domain.util.ByteArray;
import pt.ist.expenditureTrackingSystem.util.ReportUtils;
import pt.ist.fenixframework.pstm.Transaction;

public class AcquisitionRequestDocument extends AcquisitionRequestDocument_Base {

    protected AcquisitionRequestDocument() {
	super();
	setRequestNumber(readNextRequestNumber());
    }

    public AcquisitionRequestDocument(final AcquisitionRequest acquisitionRequest) {
	this();
	setAcquisitionRequest(acquisitionRequest);

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("acquisitionRequest", acquisitionRequest);
	paramMap.put("requestNumber", getRequestNumber());
	try {
	    byte[] byteArray = ReportUtils.exportToPdfFileAsByteArray("acquisitionRequestDocument", paramMap, ResourceBundle
		    .getBundle("resources/AcquisitionResources"), acquisitionRequest.getAcquisitionRequestItemsSet());
	    setContent(new ByteArray(byteArray));
	    setFilename(getRequestNumber() + ".pdf");
	} catch (JRException e) {
	    throw new DomainException("error.creating.acquisition.request.document");
	}
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
