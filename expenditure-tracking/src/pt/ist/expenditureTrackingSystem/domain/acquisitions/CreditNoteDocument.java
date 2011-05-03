package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import myorg.applicationTier.Authenticate.UserView;
import myorg.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

@ClassNameBundle(bundle = "resources/AcquisitionResources")
public class CreditNoteDocument extends CreditNoteDocument_Base {

    public CreditNoteDocument(String displayName, String filename, byte[] content) {
	super();
	init(displayName, filename, content);
    }

    @Override
    public void validateUpload(WorkflowProcess workflowProcess) {
	if (!ExpenditureTrackingSystem.isAcquisitionCentralGroupMember()) {
	    throw new ProcessFileValidationException("resources/AcquisitionResources", "error.creditNoteDocument.upload.invalid");
	}
    }

    @Override
    public boolean isPossibleToArchieve() {
	return ExpenditureTrackingSystem.isAcquisitionCentralGroupMember();
    }

    @Override
    public boolean isConnectedToCurrentHost() {
	final GenericProcess genericProcess = (GenericProcess) getProcess();
	return genericProcess != null && genericProcess.isConnectedToCurrentHost();
    }

}
