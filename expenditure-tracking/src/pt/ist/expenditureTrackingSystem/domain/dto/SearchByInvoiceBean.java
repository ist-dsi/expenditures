package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestWithPayment;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

public class SearchByInvoiceBean implements Serializable {

    String invoiceId;

    public String getInvoiceId() {
	return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
	this.invoiceId = invoiceId.trim();
    }

    public ArrayList<PaymentProcess> search() {
	ArrayList<PaymentProcess> resultsList = new ArrayList<PaymentProcess>();
	if (StringUtils.isEmpty(invoiceId))
	    return resultsList;

	List<PaymentProcessYear> processesYears = ExpenditureTrackingSystem.getInstance().getPaymentProcessYears();
	
	for (PaymentProcessYear year : processesYears) {
	    for (GenericProcess process : year.getPaymentProcessSet()) {
		if (process instanceof PaymentProcess/*
						      * PaymentProcess.class.
						      * isAssignableFrom
						      * (process.getClass())
						      */) {
		    RequestWithPayment request = ((PaymentProcess) process).getRequest();
		    if (request != null) {
			for (PaymentProcessInvoice invoice : request.getInvoices()) {
			    if (invoice.getInvoiceNumber().equals(invoiceId)) {
				resultsList.add((PaymentProcess) process);
				break;
			    }
			}
		    }
		}
	    }
	}
	return resultsList;
    }
}
