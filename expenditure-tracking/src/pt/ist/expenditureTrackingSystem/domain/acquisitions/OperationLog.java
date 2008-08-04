package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class OperationLog extends OperationLog_Base {

    public OperationLog(AcquisitionProcess process, Person person, String operation, AcquisitionProcessStateType state,
	    DateTime when) {
	super();
	super.setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	super.setAcquisitionProcess(process);
	super.setOperation(operation);
	super.setExecutor(person);
	super.setState(state);
	super.setWhenOperationWasRan(when);
    }

    public GenericAcquisitionProcessActivity getActivity() {
	return getAcquisitionProcess().getActivityByName(getOperation());
    }
    
    @Override
    public void setOperation(String operation) {
	throw new DomainException("error.unable.to.change.operation");
    }

    @Override
    public void setAcquisitionProcess(AcquisitionProcess acquisitionProcess) {
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
