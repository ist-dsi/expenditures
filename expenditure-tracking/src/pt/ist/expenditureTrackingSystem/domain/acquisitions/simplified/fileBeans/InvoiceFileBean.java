package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.fileBeans;

import java.util.ArrayList;
import java.util.List;

import module.workflow.domain.WorkflowProcess;
import module.workflow.util.WorkflowFileUploadBean;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;

public class InvoiceFileBean extends WorkflowFileUploadBean {

    private String invoiceNumber;
    private LocalDate invoiceDate;
    private List<AcquisitionRequestItem> items;
    private AcquisitionRequest request;
    private Boolean hasMoreInvoices = Boolean.FALSE;

    public Boolean getHasMoreInvoices() {
	return hasMoreInvoices;
    }

    public void setHasMoreInvoices(Boolean hasMoreInvoices) {
	this.hasMoreInvoices = hasMoreInvoices;
    }

    public InvoiceFileBean(WorkflowProcess process) {
	super(process);
	AcquisitionRequest request = ((PaymentProcess) process).getRequest();
	setRequest(request);
	setItems(new ArrayList<AcquisitionRequestItem>(request.getAcquisitionRequestItemsSet()));
    }

    public InvoiceFileBean(AcquisitionRequest request) {
	super(request.getProcess());
	setRequest(request);
	setItems(new ArrayList<AcquisitionRequestItem>(request.getAcquisitionRequestItemsSet()));
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

    public void setItems(List<AcquisitionRequestItem> items) {
	this.items = new ArrayList<AcquisitionRequestItem>();
	this.items.addAll(items);
    }

    public List<AcquisitionRequestItem> getItems() {
	return this.items;
    }

    public void setRequest(AcquisitionRequest request) {
	this.request = request;
    }

    public AcquisitionRequest getRequest() {
	return this.request;
    }

    @Override
    public boolean isDefaultUploadInterfaceUsed() {
	return false;
    }
}
