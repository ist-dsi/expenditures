package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;
import myorg.applicationTier.Authenticate.UserView;
import myorg.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.fileBeans.AcquisitionProposalDocumentFileBean;

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
	PaymentProcess process = (PaymentProcess) workflowProcess;
	if (!process.isInGenesis() || process.getProcessCreator() != UserView.getCurrentUser()) {
	    throw new ProcessFileValidationException("resources/AcquisitionResources",
		    "error.acquisitionProposalDocument.upload.invalid");
	}
	super.validateUpload(workflowProcess);
    }

    @Override
    public boolean isPossibleToArchieve() {
	PaymentProcess process = (PaymentProcess) getProcess();
	return process.isInGenesis() && process.getProcessCreator() == UserView.getCurrentUser();
    }
}
