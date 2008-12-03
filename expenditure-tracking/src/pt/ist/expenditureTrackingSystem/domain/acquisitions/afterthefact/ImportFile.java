package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;

public class ImportFile extends ImportFile_Base {

    public ImportFile(byte[] bytes, String displayName) {
	super();
	setContent(bytes);
	setDisplayName(displayName);
	setActive(Boolean.TRUE);
    }

    @Service
    public void delete() {
	if (getAfterTheFactAcquisitionProcessesCount() > 0) {
	    throw new DomainException("exception.domain.ImportFile.cannotDeleteImportFileWithProcesses");
	}

	Transaction.deleteObject(this);
    }

    @Service
    public void cancel() {
	if (getAfterTheFactAcquisitionProcessesCount() == 0) {
	    delete();

	} else {
	    for (AfterTheFactAcquisitionProcess process : getAfterTheFactAcquisitionProcesses()) {
		process.cancel();
	    }
	    setActive(Boolean.FALSE);
	}
    }

    @Service
    public void reenable() {
	setActive(Boolean.TRUE);
	for (AfterTheFactAcquisitionProcess process : getAfterTheFactAcquisitionProcesses()) {
	    process.renable();
	}

    }

}
