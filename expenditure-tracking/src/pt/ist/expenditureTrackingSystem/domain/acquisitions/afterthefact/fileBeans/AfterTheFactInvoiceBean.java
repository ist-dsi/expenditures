package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.fileBeans;

import module.workflow.domain.WorkflowProcess;
import module.workflow.util.WorkflowFileUploadBean;

import org.joda.time.LocalDate;

public class AfterTheFactInvoiceBean extends WorkflowFileUploadBean {

    private String invoiceNumber;
    private LocalDate invoiceDate;

    public AfterTheFactInvoiceBean(WorkflowProcess process) {
	super(process);
    }

    public String getInvoiceNumber() {
	return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
	return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
	this.invoiceDate = invoiceDate;
    }

}
