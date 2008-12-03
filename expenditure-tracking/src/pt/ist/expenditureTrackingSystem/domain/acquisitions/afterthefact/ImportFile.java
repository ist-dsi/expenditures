package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.fenixframework.pstm.Transaction;

public class ImportFile extends ImportFile_Base {

    public ImportFile(byte[] bytes, String displayName) {
	super();
	setContent(bytes);
	setDisplayName(displayName);
    }

    public void delete() {
	if (getAfterTheFactAcquisitionProcessesCount() > 0) {
	    throw new DomainException("exception.domain.ImportFile.cannotDeleteImportFileWithProcesses");
	}

	Transaction.deleteObject(this);
    }

}
