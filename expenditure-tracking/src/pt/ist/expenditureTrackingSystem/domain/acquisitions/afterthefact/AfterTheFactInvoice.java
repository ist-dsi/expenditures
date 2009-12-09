package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;
import myorg.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.fileBeans.AfterTheFactInvoiceBean;
import pt.ist.fenixWebFramework.services.Service;

@ClassNameBundle(bundle = "resources/AcquisitionResources")
public class AfterTheFactInvoice extends AfterTheFactInvoice_Base {

    static {
	FileUploadBeanResolver.registerBeanForProcessFile(AfterTheFactInvoice.class, AfterTheFactInvoiceBean.class);
    }

    public AfterTheFactInvoice(String displayName, String filename, byte[] content) {
	super();
	init(displayName, filename, content);
    }

    public AfterTheFactInvoice(final AfterTheFactAcquisitionProcess process) {
	super();
	process.addFiles(this);
    }

    public AfterTheFactInvoice() {
	super();
    }

    @Override
    public void fillInNonDefaultFields(WorkflowFileUploadBean bean) {
	AfterTheFactInvoiceBean invoiceBean = (AfterTheFactInvoiceBean) bean;
	setInvoiceDate(invoiceBean.getInvoiceDate());
	setInvoiceNumber(invoiceBean.getInvoiceNumber());
    }

    @Override
    public void validateUpload(WorkflowProcess workflowProcess) {
	AfterTheFactAcquisitionProcess process = (AfterTheFactAcquisitionProcess) workflowProcess;
	if (process.getInvoice() != null) {
	    throw new ProcessFileValidationException("resources/AcquisitionResources",
		    "error.message.cannotHaveMoreThanOneInvoice");
	}
    }

    @Override
    @Service
    public void delete() {
	removeProcess();
	super.delete();
    }
}
