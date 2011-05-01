package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;
import myorg.applicationTier.Authenticate.UserView;
import myorg.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.fileBeans.AcquisitionProposalDocumentFileBean;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

@ClassNameBundle(bundle = "resources/AcquisitionResources")
public class AcquisitionProposalDocument extends AcquisitionProposalDocument_Base {

    static {
	FileUploadBeanResolver.registerBeanForProcessFile(AcquisitionProposalDocument.class,
		AcquisitionProposalDocumentFileBean.class);
    }

    public AcquisitionProposalDocument(String displayName, String filename, byte[] content) {
	super();
	init(displayName, filename, content);
    }

    public AcquisitionProposalDocument() {
	super();
    }

    public void delete() {
	throw new UnsupportedOperationException();
    }

    @Override
    public void fillInNonDefaultFields(WorkflowFileUploadBean bean) {
	AcquisitionProposalDocumentFileBean fileBean = (AcquisitionProposalDocumentFileBean) bean;
	setProposalId(fileBean.getProposalID());
	setDisplayName(fileBean.getProposalID());
    }

    @Override
    public void validateUpload(WorkflowProcess workflowProcess) {
	SimplifiedProcedureProcess process = (SimplifiedProcedureProcess) workflowProcess;

	if (!process.getFiles(AcquisitionProposalDocument.class).isEmpty()) {
	    throw new ProcessFileValidationException("resources/AcquisitionResources",
		    "error.acquisitionProposalDocument.allowedOnlyOneProposal");
	}
	if (process.getProcessClassification() != ProcessClassification.CT75000
		&& (!process.isInGenesis() || process.getProcessCreator() != UserView.getCurrentUser())) {
	    throw new ProcessFileValidationException("resources/AcquisitionResources",
		    "error.acquisitionProposalDocument.upload.invalid");
	}
	if (process.getProcessClassification() == ProcessClassification.CT75000
		&& !((process.isAuthorized() || process.isAcquisitionProcessed()) && UserView.getCurrentUser()
			.getExpenditurePerson().hasRoleType(RoleType.ACQUISITION_CENTRAL))) {
	    throw new ProcessFileValidationException("resources/AcquisitionResources",
		    "error.acquisitionProposalDocument.ct75000.upload.invalid");
	}

	super.validateUpload(workflowProcess);
    }

    @Override
    public boolean isPossibleToArchieve() {
	SimplifiedProcedureProcess process = (SimplifiedProcedureProcess) getProcess();
	return process.getProcessClassification() != ProcessClassification.CT75000 ? process.isInGenesis()
		&& process.getProcessCreator() == UserView.getCurrentUser() : (process.isAuthorized() || process
		.isAcquisitionProcessed())
		&& UserView.getCurrentUser().getExpenditurePerson().hasRoleType(RoleType.ACQUISITION_CENTRAL);
    }

    @Override
    public boolean isConnectedToCurrentHost() {
	final GenericProcess genericProcess = (GenericProcess) getProcess();
	return genericProcess != null && genericProcess.isConnectedToCurrentHost();
    }

}
