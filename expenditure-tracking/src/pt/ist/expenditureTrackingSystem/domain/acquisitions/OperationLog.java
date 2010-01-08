package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;
import myorg.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class OperationLog extends OperationLog_Base {

    public OperationLog(AcquisitionProcess process, User user, String operation, AcquisitionProcessStateType type) {

	super();
	init(process, user);
	super.setOperation(operation);
	super.setState(type);
    }

    @Override
    public void setOperation(String operation) {
	throw new DomainException("error.unable.to.change.operation");
    }

    @Override
    public void setProcess(WorkflowProcess process) {
	throw new DomainException("error.unable.to.change.process");
    }

    @Override
    public void setActivityExecutor(User executor) {
	throw new DomainException("error.unable.to.change.executor");
    }

    @Override
    public void setWhenOperationWasRan(DateTime when) {
	throw new DomainException("error.unable.to.change.when.operation.was.executed");
    }

    public void delete() {
	super.setActivityExecutor(null);
	super.setProcess(null);
	deleteDomainObject();
    }

}
