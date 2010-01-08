package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;
import myorg.applicationTier.Authenticate.UserView;
import myorg.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.fileBeans.InvoiceFileBean;

@ClassNameBundle(bundle = "resources/AcquisitionResources")
public class AcquisitionInvoice extends AcquisitionInvoice_Base {

    static {
	FileUploadBeanResolver.registerBeanForProcessFile(AcquisitionInvoice.class, InvoiceFileBean.class);
    }

    public AcquisitionInvoice(String displayName, String filename, byte[] content) {
	super();
	init(displayName, filename, content);
    }

    @Override
    public void delete() {
	throw new UnsupportedOperationException();
    }

    @Override
    public void fillInNonDefaultFields(WorkflowFileUploadBean bean) {
	super.fillInNonDefaultFields(bean);

	InvoiceFileBean fileBean = (InvoiceFileBean) bean;
	AcquisitionRequest request = fileBean.getRequest();
	request.validateInvoiceNumber(fileBean.getInvoiceNumber());

	setInvoiceNumber(fileBean.getInvoiceNumber());
	setInvoiceDate(fileBean.getInvoiceDate());

	setInvoiceDate(fileBean.getInvoiceDate());
	for (AcquisitionRequestItem item : fileBean.getItems()) {
	    addRequestItems(item);
	}

    }

    @Override
    public void validateUpload(WorkflowProcess workflowProcess) {
	RegularAcquisitionProcess process = (RegularAcquisitionProcess) workflowProcess;
	if (!process.isAcquisitionProcessed()
		|| !UserView.getCurrentUser().getExpenditurePerson().hasRoleType(RoleType.ACQUISITION_CENTRAL)) {
	    throw new ProcessFileValidationException("resources/AcquisitionResources", "error.acquisitionInvoice.upload.invalid");
	}
    }

    @Override
    public void postProcess(WorkflowFileUploadBean bean) {
	InvoiceFileBean fileBean = (InvoiceFileBean) bean;
	AcquisitionRequest request = fileBean.getRequest();
	if (!fileBean.getHasMoreInvoices()) {
	    ((RegularAcquisitionProcess) request.getProcess()).invoiceReceived();
	}
	request.processReceivedInvoice();
    }

    @Override
    public boolean isPossibleToArchieve() {
	RegularAcquisitionProcess process = (RegularAcquisitionProcess) getProcess();
	return (process.isAcquisitionProcessed() || process.isInvoiceReceived())
		&& UserView.getCurrentUser().getExpenditurePerson().hasRoleType(RoleType.ACQUISITION_CENTRAL);
    }

    @Override
    public String getDisplayName() {
	return getFilename();
    }

}
