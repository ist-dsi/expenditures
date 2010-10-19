package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.fileBeans;

import java.io.Serializable;
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
    private List<RequestItemHolder> items;
    private AcquisitionRequest request;
    private Boolean hasMoreInvoices = Boolean.FALSE;

    public class RequestItemHolder implements Serializable {
	private boolean accountable;
	private AcquisitionRequestItem item;
	private String description;
	private int amount;

	RequestItemHolder(AcquisitionRequestItem item) {
	    this.accountable = true;
	    this.description = item.getDescription();
	    this.item = item;
	    this.amount = item.getCurrentQuantity();
	}

	public String getDescription() {
	    return description;
	}

	public void setDescription(String description) {
	    this.description = description;
	}

	public boolean isAccountable() {
	    return accountable;
	}

	public void setAccountable(boolean accountable) {
	    this.accountable = accountable;
	}

	public AcquisitionRequestItem getItem() {
	    return item;
	}

	public void setItem(AcquisitionRequestItem item) {
	    this.item = item;
	}

	public int getAmount() {
	    return amount;
	}

	public void setAmount(int amount) {
	    this.amount = amount;
	}

    }

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
	this.items = new ArrayList<RequestItemHolder>();
	for (AcquisitionRequestItem item : request.getAcquisitionRequestItemsSet()) {
	    this.items.add(new RequestItemHolder(item));
	}
    }

    public InvoiceFileBean(AcquisitionRequest request) {
	super(request.getProcess());
	setRequest(request);
	this.items = new ArrayList<RequestItemHolder>();
	for (AcquisitionRequestItem item : request.getAcquisitionRequestItemsSet()) {
	    this.items.add(new RequestItemHolder(item));
	}
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

    public List<RequestItemHolder> getItems() {
	return items;
    }

    public void setItems(List<RequestItemHolder> items) {
	this.items = items;
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
