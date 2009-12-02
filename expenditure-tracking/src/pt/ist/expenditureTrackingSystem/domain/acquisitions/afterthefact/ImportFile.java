package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact;

import myorg.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;

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

	deleteDomainObject();
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
