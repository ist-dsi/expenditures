package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;



public class Invoice extends Invoice_Base {
    
    public Invoice() {
	super();
    }

    public boolean isInvoiceReceived() {
	return getInvoiceNumber() != null && getInvoiceNumber().length() > 0 && getInvoiceDate() != null;
    }

    public void delete() {
	super.delete();
    }

    @Override
    public boolean isConnectedToCurrentHost() {
	final GenericProcess genericProcess = (GenericProcess) getProcess();
	return genericProcess != null && genericProcess.isConnectedToCurrentHost();
    }

}
