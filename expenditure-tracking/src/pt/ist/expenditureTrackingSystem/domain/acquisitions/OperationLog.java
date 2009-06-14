package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixframework.pstm.Transaction;

public class OperationLog extends OperationLog_Base {

    public OperationLog() {
	super();
    }
    
    public OperationLog(AcquisitionProcess process, Person person, String operation, 
	    DateTime when, AcquisitionProcessStateType type) {
	super();
	init(process, person, operation, when);
	setState(type);
    }
    
    @Override
    public <T extends GenericProcess> AbstractActivity<T> getActivity() {
	AcquisitionProcess process = (AcquisitionProcess) getProcess();
	return process.getActivityByName(getOperation());
    }

    @Override
    public void setOperation(String operation) {
	throw new DomainException("error.unable.to.change.operation");
    }

    @Override
    public void setProcess(GenericProcess process) {
	throw new DomainException("error.unable.to.change.process");
    }

    @Override
    public void setExecutor(Person executor) {
	throw new DomainException("error.unable.to.change.executor");
    }

    @Override
    public void setWhenOperationWasRan(DateTime when) {
	throw new DomainException("error.unable.to.change.when.operation.was.executed");
    }

    public void delete() {
	super.setExecutor(null);
	super.setProcess(null);
	super.removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

}
