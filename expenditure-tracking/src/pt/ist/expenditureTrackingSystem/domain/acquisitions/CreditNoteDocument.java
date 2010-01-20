package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import myorg.applicationTier.Authenticate.UserView;
import myorg.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.RoleType;

@ClassNameBundle(bundle = "resources/AcquisitionResources")
public class CreditNoteDocument extends CreditNoteDocument_Base {

    public CreditNoteDocument(String displayName, String filename, byte[] content) {
	super();
	init(displayName, filename, content);
    }

    @Override
    public void validateUpload(WorkflowProcess workflowProcess) {
	RegularAcquisitionProcess process = (RegularAcquisitionProcess) workflowProcess;
	if (!process.isAcquisitionProcessed()
		|| !UserView.getCurrentUser().getExpenditurePerson().hasRoleType(RoleType.ACQUISITION_CENTRAL)) {
	    throw new ProcessFileValidationException("resources/AcquisitionResources", "error.creditNoteDocument.upload.invalid");
	}
    }

    @Override
    public boolean isPossibleToArchieve() {
	RegularAcquisitionProcess process = (RegularAcquisitionProcess) getProcess();
	return (process.isAcquisitionProcessed() || process.isInvoiceReceived())
		&& UserView.getCurrentUser().getExpenditurePerson().hasRoleType(RoleType.ACQUISITION_CENTRAL);
    }

}
