package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

public class OperationLog extends OperationLog_Base {

    public OperationLog(AcquisitionProcess process, Person person, String operation, AcquisitionProcessStateType state,
	    DateTime when) {
	super();
	init(process, person, operation, when);
	super.setState(state);
    }

    @Override
    public GenericAcquisitionProcessActivity getActivity() {
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

    @Override
    public void setState(AcquisitionProcessStateType state) {
	throw new DomainException("error.unable.to.change.when.state");
    }
    
}
