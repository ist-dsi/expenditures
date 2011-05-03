package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;
import myorg.applicationTier.Authenticate.UserView;
import myorg.util.BundleUtil;
import myorg.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.fileBeans.InvoiceFileBean;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.fileBeans.InvoiceFileBean.RequestItemHolder;

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
	StringBuilder builder = new StringBuilder("<ul>");
	for (RequestItemHolder itemHolder : fileBean.getItems()) {
	    if (itemHolder.isAccountable()) {
		addRequestItems(itemHolder.getItem());
		builder.append("<li>");
		builder.append(itemHolder.getDescription());
		builder.append(" - ");
		builder.append(BundleUtil.getFormattedStringFromResourceBundle("resources/AcquisitionResources",
			"acquisitionRequestItem.label.quantity"));
		builder.append(":");
		builder.append(itemHolder.getAmount());
		builder.append("</li>");
	    }
	}
	builder.append("</ul>");
	setConfirmationReport(builder.toString());
    }

    @Override
    public void validateUpload(WorkflowProcess workflowProcess) {
	RegularAcquisitionProcess process = (RegularAcquisitionProcess) workflowProcess;
	if (!process.isAcquisitionProcessed()
		|| !ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(UserView.getCurrentUser())) {
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
		&& ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(UserView.getCurrentUser());
    }

    @Override
    public String getDisplayName() {
	return getFilename();
    }

    @Override
    public void processRemoval() {
	for (; !getFinancers().isEmpty(); getFinancers().get(0).removeAllocatedInvoices(this))
	    ;
	for (; !getProjectFinancers().isEmpty(); getProjectFinancers().get(0).removeAllocatedInvoices(this))
	    ;
	for (; !getRequestItems().isEmpty(); getRequestItems().get(0).removeInvoicesFiles(this))
	    ;
	for (; !getUnitItems().isEmpty(); getUnitItems().get(0).removeConfirmedInvoices(this))
	    ;
    }

}
