package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ArrayList;
import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericLog;

public abstract class RegularAcquisitionProcess extends RegularAcquisitionProcess_Base {

    public RegularAcquisitionProcess() {
	super();
    }

    public boolean isSimplifiedAcquisitionProcess() {
	return false;
    }

    public boolean isStandardAcquisitionProcess() {
	return false;
    }

    public List<OperationLog> getOperationLogsInState(Enum state) {
	List<OperationLog> logs = new ArrayList<OperationLog>();
	for (OperationLog log : getOperationLogs()) {
	    if (log.getLogState().equals(state)) {
		logs.add(log);
	    }
	}
	return logs;
    }

    public List<OperationLog> getOperationLogs() {
	List<OperationLog> logs = new ArrayList<OperationLog>();
	for (GenericLog log : super.getExecutionLogs()) {
	    logs.add((OperationLog) log);
	}
	return logs;
    }

    public void approveBy(Person person) {
	getAcquisitionRequest().approvedBy(person);
	if (getAcquisitionRequest().isApprovedByAllResponsibles()) {
	    approve();
	}
    }

    public void confirmInvoiceBy(Person person) {
	getAcquisitionRequest().confirmInvoiceFor(person);
	if (getAcquisitionRequest().isInvoiceConfirmedBy()) {
	    confirmInvoice();
	}
    }

    public abstract void cancel();

    public abstract void reject();

    public abstract void inGenesis();

    public abstract void submitForApproval();

    public abstract void submitForFundAllocation();

    protected abstract void approve();

    public abstract void allocateFundsToUnit();

    public abstract void allocateFundsToSupplier();

    public abstract void processAcquisition();

    public abstract void invoiceReceived();

    public abstract void submitedForInvoiceConfirmation();

    protected abstract void confirmInvoice();

    public abstract void allocateFundsPermanently();

    public abstract void acquisitionPayed();

    public abstract void resetEffectiveFundAllocationId();
}
